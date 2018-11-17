/**
 * Copyright (c) 2015-2016, Michael Yang 杨福海 (fuhai999@gmail.com).
 *
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.abreaking.front.controller;

import com.jfinal.render.Render;
import com.abreaking.common.consts.Consts;
import com.abreaking.core.BaseFrontController;
import com.abreaking.core.addon.HookInvoker;
import com.abreaking.core.cache.ActionCacheManager;
import com.abreaking.message.Actions;
import com.abreaking.message.MessageKit;
import com.abreaking.model.Content;
import com.abreaking.model.Taxonomy;
import com.abreaking.model.query.ContentQuery;
import com.abreaking.model.query.TaxonomyQuery;
import com.abreaking.router.RouterMapping;
import com.abreaking.template.TemplateManager;
import com.abreaking.template.TplModule;
import com.abreaking.freemarker.tag.CommentPageTag;
import com.abreaking.freemarker.tag.MenusTag;
import com.abreaking.freemarker.tag.NextContentTag;
import com.abreaking.freemarker.tag.PreviousContentTag;
import com.abreaking.common.util.StringUtils;

import java.math.BigInteger;
import java.util.List;

@RouterMapping(url = Consts.ROUTER_CONTENT)
public class ContentController extends BaseFrontController {

	private String slug;
	private BigInteger id;
	private int page;

	private boolean hasUser;


	/*@ActionCache*/
	public void index() {
		try {
			Render render = onRenderBefore();
			if (render != null) {
				render(render);
			} else {
				doRender();
			}
		} finally {
			onRenderAfter();
		}

	}

	private void doRender() {
		initRequest();

		String user = getCookie("user");
		if(StringUtils.isNotBlank(user)){
			hasUser = true;
		}

		Content content = queryContent();
		if (null == content) {
			renderError(404);
			return;
		}

		TplModule module = TemplateManager.me().currentTemplateModule(content.getModule());

		if (module == null) {
			renderError(404);
			return;
		}

		updateContentViewCount(content);
		setGlobleAttrs(content);

		setAttr("p", page);
		setAttr("content", content);
		
		setAttr(NextContentTag.TAG_NAME, new NextContentTag(content));
		setAttr(PreviousContentTag.TAG_NAME, new PreviousContentTag(content));
		
		setAttr(CommentPageTag.TAG_NAME, new CommentPageTag(getRequest(), content, page));

		List<Taxonomy> taxonomys = TaxonomyQuery.me().findListByContentId(content.getId());
		setAttr("taxonomys", taxonomys);
		setAttr(MenusTag.TAG_NAME, new MenusTag(getRequest(), taxonomys, content));

		String style = content.getStyle();
		if (StringUtils.isNotBlank(style)) {
			render(String.format("content_%s_%s.html", module.getName(), style.trim()));
			return;
		}

		if (taxonomys != null && !taxonomys.isEmpty()) {
			String forSlug = null;
			for (Taxonomy taxonomy : taxonomys) {
				String tFile = String.format("content_%s_for:%s.html", module.getName(), taxonomy.getSlug());
				if (templateExists(tFile)) {
					if (forSlug == null) {
						forSlug = "for:" + taxonomy.getSlug();
					} else {
						forSlug = null;
						break;
					}
				}
			}

			if (forSlug != null) {
				render(String.format("content_%s_%s.html", module.getName(), forSlug));
				return;
			}
		}

		render(String.format("content_%s.html", module.getName()));

	}

	/**
	 * 每次用户登陆，我都会指定一个id给该用户，如果该用户有访问某篇文章，那么该文章阅读数+1,
	 * @param content
	 * @param userid
	 */
	private void updateContentViewCount(Content content,String userid){

	}

	private void updateContentViewCount(Content content) {
		long visitorCount = VisitorCounter.getVisitorCount(content.getId());

		//如果是我自己在登陆操作，啥也不做
		if(hasUser){
			return ;
		}

		//TODO 这个还不知怎么做呢，先加1吧
		/*Long viewCount = content.getViewCount() == null ? visitorCount : content.getViewCount() + visitorCount;*/
		Long viewCount = content.getViewCount() == null ? visitorCount : content.getViewCount() + 1;
		content.setViewCount(viewCount);
		if (content.update()) {
			VisitorCounter.clearVisitorCount(content.getId());
		}
		MessageKit.sendMessage(Actions.COMMENT_ADD, content);
		ActionCacheManager.clearCache();
	}

	private void setGlobleAttrs(Content content) {

		setAttr(Consts.ATTR_GLOBAL_WEB_TITLE, content.getTitle());

		if (StringUtils.isNotBlank(content.getMetaKeywords())) {
			setAttr(Consts.ATTR_GLOBAL_META_KEYWORDS, content.getMetaKeywords());
		} else {
			setAttr(Consts.ATTR_GLOBAL_META_KEYWORDS, content.getTaxonomyAsString(null));
		}

		if (StringUtils.isNotBlank(content.getMetaDescription())) {
			setAttr(Consts.ATTR_GLOBAL_META_DESCRIPTION, content.getMetaDescription());
		} else {
			setAttr(Consts.ATTR_GLOBAL_META_DESCRIPTION, content.getSummary());
		}

	}

	private Content queryContent() {
		if (id != null) {
			return ContentQuery.me().findById(id);
		} else {
			return ContentQuery.me().findBySlug(StringUtils.urlDecode(slug));
		}
	}

	private void initRequest() {
		String para = getPara(0);
		if (StringUtils.isBlank(para)) {
			id = getParaToBigInteger("id");
			slug = getPara("slug");
			page = getParaToInt("p", 1);
			if (id == null && slug == null) {
				renderError(404);
			}
			return;
		}

		if (StringUtils.isNumeric(para)) {
			id = new BigInteger(para);
		} else {
			slug = para;
		}
		page = getParaToInt(1, 1);

	}

	private Render onRenderBefore() {
		return HookInvoker.contentRenderBefore(this);
	}

	private void onRenderAfter() {
		HookInvoker.contentRenderAfter(this);
	}

}

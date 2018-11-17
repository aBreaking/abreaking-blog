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
import com.abreaking.model.query.OptionQuery;
import com.abreaking.router.RouterMapping;
import com.abreaking.freemarker.tag.IndexPageTag;
import com.abreaking.common.util.StringUtils;

@RouterMapping(url = "/")
public class IndexController extends BaseFrontController {

	/*@ActionCache*/
	public void index() {
		try {
			String ipAddress = this.getIPAddress();
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
		setGlobleAttrs();
		String para = getPara();

		if (StringUtils.isBlank(para)) {
			setAttr(IndexPageTag.TAG_NAME, new IndexPageTag(getRequest(), null, 1, null));
			render("index.html");
			return;
		}

		String[] paras = para.split("-");
		if (paras.length == 1) {
			if (StringUtils.isNumeric(para.trim())) {
				setAttr(IndexPageTag.TAG_NAME, new IndexPageTag(getRequest(), null, StringUtils.toInt(para.trim(), 1), null));
				render("index.html");
			} else {
				setAttr(IndexPageTag.TAG_NAME, new IndexPageTag(getRequest(), para.trim(), 1, null));
				render("page_" + para + ".html");
			}
		} else if (paras.length == 2) {
			String pageName = paras[0];
			String pageNumber = paras[1];

			if (!StringUtils.isNumeric(pageNumber)) {
				renderError(404);
			}

			setAttr(IndexPageTag.TAG_NAME, new IndexPageTag(getRequest(), pageName, StringUtils.toInt(pageNumber, 1), null));
			render("page_" + pageName + ".html");
		} else {
			renderError(404);
		}

	}

	private void setGlobleAttrs() {
		String title = OptionQuery.me().findValue("seo_index_title");
		String keywords = OptionQuery.me().findValue("seo_index_keywords");
		String description = OptionQuery.me().findValue("seo_index_description");

		if (StringUtils.isNotBlank(title)) {
			setAttr(Consts.ATTR_GLOBAL_WEB_TITLE, title);
		}

		if (StringUtils.isNotBlank(keywords)) {
			setAttr(Consts.ATTR_GLOBAL_META_KEYWORDS, keywords);
		}

		if (StringUtils.isNotBlank(description)) {
			setAttr(Consts.ATTR_GLOBAL_META_DESCRIPTION, description);
		}
	}

	private Render onRenderBefore() {
		return HookInvoker.indexRenderBefore(this);
	}

	private void onRenderAfter() {
		HookInvoker.indexRenderAfter(this);
	}

}

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
package io.jpress.ui.freemarker.tag;

import java.math.BigInteger;
import java.util.List;

import io.jpress.core.render.freemarker.JTag;
import io.jpress.model.Content;
import io.jpress.model.query.ContentQuery;

/**
 * @title Contents 标签
 * 
 *        使用方法：<br />
 *        <@contents orderBy="" keyword="Jpress" page="" tag="tag1,xxx"
 *        pagesize="" typeid="1,2" module="article,bbs" style=
 *        "article,video,audio" userid="123" parentid="1" userid="" ><br>
 *        <br>
 *        <#list contents as content><br>
 *        ${content.id} : ${content.title!} <br>
 *        <//#list><br>
 *        <br>
 *        </@contents>
 * 
 * 
 *        orderBy 的值有：views,lastpost,created,vote_up,vote_down
 *
 * by liwei:
 * 推荐机制：先同类型的文章推荐，每次推荐3篇，根据阅读量优先推荐。
 * 			此外，为每一个用户的session或者cookie记录已经读过的文章，已读过的文章不考虑不再推荐或者全部看完后再推荐把。
 * 
 */
public class ContentsTag extends JTag {
	
	public static final String TAG_NAME = "jp.contents";

	@Override
	public void onRender() {

		String orderBy = getParam("orderBy");
		String keyword = getParam("keyword");

		int pageNumber = getParamToInt("page", 1);
		int pageSize = getParamToInt("pageSize", 10);

		Integer count = getParamToInt("count");
		if (count != null && count > 0) {
			pageSize = count;
		}

		BigInteger[] typeIds = getParamToBigIntegerArray("typeId");
		String[] typeSlugs = getParamToStringArray("typeSlug");
		String[] tags = getParamToStringArray("tag");
		String[] modules = getParamToStringArray("module");
		String[] styles = getParamToStringArray("style");
		String[] flags = getParamToStringArray("flag");
		String[] slugs = getParamToStringArray("slug");
		BigInteger[] userIds = getParamToBigIntegerArray("userId");
		BigInteger[] parentIds = getParamToBigIntegerArray("parentId");
		Boolean hasThumbnail = getParamToBool("hasThumbnail");
		String[] category = getParamToStringArray("category");	//类型
		List<Content> data = ContentQuery.me().findListInNormal(pageNumber, pageSize, orderBy, keyword, typeIds, typeSlugs,
				modules, styles, flags, slugs, userIds, parentIds, tags,hasThumbnail,null);

		if(data==null || data.isEmpty()){
			renderText("");
			return;
		}
		
		setVariable("contents", data);
		renderBody();
	}

}

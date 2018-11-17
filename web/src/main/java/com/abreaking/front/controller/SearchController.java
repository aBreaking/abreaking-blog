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

import com.abreaking.common.consts.Consts;
import com.abreaking.core.BaseFrontController;
import com.abreaking.core.cache.ActionCache;
import com.abreaking.router.RouterMapping;
import com.abreaking.template.TemplateManager;
import com.abreaking.common.util.StringUtils;

import java.io.UnsupportedEncodingException;

@RouterMapping(url = "/s")
public class SearchController extends BaseFrontController {

	@ActionCache
	public void index() throws UnsupportedEncodingException {
		keepPara();
		//搜索范围：文章标题，文章内容
		String query = getPara("query");

		/*String keyword = getPara("k");*/
		String  keyword = query;
		if (StringUtils.isBlank(keyword)) {
			renderError(404);
		}

		String moduleName = getPara("m", Consts.MODULE_ARTICLE);
		if (TemplateManager.me().currentTemplateModule(moduleName) == null) {
			renderError(404);
		}

		int pageNumber = getParaToInt("p", 1);
		pageNumber = pageNumber < 1 ? 1 : pageNumber;

		setAttr("keyword", StringUtils.escapeHtml(keyword));
		/*setAttr(SearchResultPageTag.TAG_NAME, new SearchResultPageTag(keyword, moduleName, pageNumber));*/
		/*render(String.format("search_%s.html", moduleName));*/

		render("index.html");
	}

}

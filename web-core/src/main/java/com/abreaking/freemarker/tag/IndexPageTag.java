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
package com.abreaking.freemarker.tag;

import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;
import com.abreaking.core.render.freemarker.BasePaginateTag;
import com.abreaking.core.render.freemarker.JTag;
import com.abreaking.model.Content;
import com.abreaking.model.query.ContentQuery;
import com.abreaking.common.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

public class IndexPageTag extends JTag {
	
	public static final String TAG_NAME = "jp.indexPage";

	int pageNumber;
	String pagePara;
	String orderBy;
	HttpServletRequest request;

	public IndexPageTag(HttpServletRequest request, String pagePara, int pageNumber, String orderBy) {
		this.pagePara = pagePara;
		if (pageNumber < 1) {
			pageNumber = 1;
		}
		this.pageNumber = pageNumber;
		this.request = request;
		this.orderBy = orderBy;
	}

	@Override
	public void onRender() {

		orderBy = StringUtils.isBlank(orderBy) ? getParam("orderBy") : orderBy;
		String keyword = getParam("keyword");

		int pagesize = getParamToInt("pageSize", 10);

		BigInteger[] typeIds = getParamToBigIntegerArray("typeId");
		String[] modules = getParamToStringArray("module");
		String status = getParam("status", Content.STATUS_NORMAL);

		Page<Content> page = ContentQuery.me().paginate(pageNumber, pagesize, modules, keyword, status, typeIds, null,orderBy);
		setVariable("page", page);
		setVariable("contents", page.getList());
		/*setVariable("liwei","haha");*/
		IndexPaginateTag pagination = new IndexPaginateTag(request, page, pagePara);
		setVariable("pagination", pagination);

		renderBody();
	}

	public static class IndexPaginateTag extends BasePaginateTag {

		String pagePara;
		HttpServletRequest request;

		public IndexPaginateTag(HttpServletRequest request, Page<Content> page, String pagePara) {
			super(page);
			this.request = request;
			this.pagePara = pagePara;
		}

		@Override
		protected String getUrl(int pageNumber) {
			String url = JFinal.me().getContextPath() + "/";

			if (StringUtils.isNotBlank(pagePara)) {
				url += pagePara + "-" + pageNumber;
			} else {
				url += pageNumber;
			}

			if (enalbleFakeStatic()) {
				url += getFakeStaticSuffix();
			}

			String queryString = request.getQueryString();
			if (StringUtils.isNotBlank(queryString)) {
				url += "?" + queryString;
			}

			if (StringUtils.isNotBlank(getAnchor())) {
				url += "#" + getAnchor();
			}

			return url;
		}

	}

}

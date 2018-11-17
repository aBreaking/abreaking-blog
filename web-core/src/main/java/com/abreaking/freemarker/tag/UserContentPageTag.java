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

import java.math.BigInteger;

public class UserContentPageTag extends JTag {
	
	public static final String TAG_NAME = "jp.userContentPage";

	BigInteger userId;
	int pageNumber;
	String action;

	public UserContentPageTag(String action, BigInteger userId, int pageNumber) {
		this.userId = userId;
		this.pageNumber = pageNumber;
		this.action = action;
	}

	@Override
	public void onRender() {

		String module = getParam("module");
		BigInteger taxonomyId = getParamToBigInteger("taxonomyId");

		int pageSize = getParamToInt("pageSize", 10);
		String orderby = getParam("orderBy");
		String status = getParam("status", Content.STATUS_NORMAL);

		BigInteger[] tids = taxonomyId == null ? null : new BigInteger[] { taxonomyId };

		Page<Content> page = ContentQuery.me().paginate(pageNumber, pageSize, module, null, status, tids, userId,orderby);
		setVariable("page", page);
		setVariable("contents", page.getList());
		
		MyPaginateTag pagination = new MyPaginateTag(page, action);
		setVariable("pagination", pagination);
		
		renderBody();
	}

	public static class MyPaginateTag extends BasePaginateTag {

		final String action;

		public MyPaginateTag(Page<Content> page, String action) {
			super(page);
			this.action = action;
		}

		@Override
		protected String getUrl(int pageNumber) {
			String url = JFinal.me().getContextPath() + "/user/center/";
			url += action;
			url += "-" + pageNumber;

			if (StringUtils.isNotBlank(getAnchor())) {
				url += "#" + getAnchor();
			}
			return url;
		}

	}

}

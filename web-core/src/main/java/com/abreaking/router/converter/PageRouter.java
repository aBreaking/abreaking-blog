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
package com.abreaking.router.converter;

import com.abreaking.common.consts.Consts;
import com.abreaking.model.Content;
import com.abreaking.model.query.ContentQuery;
import com.abreaking.router.RouterConverter;
import com.abreaking.common.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PageRouter extends RouterConverter {

	@Override
	public String converter(String target, HttpServletRequest request, HttpServletResponse response) {

		String[] targetDirs = parseTarget(target);
		if (targetDirs == null || targetDirs.length != 1) {
			return null;
		}

		String slug = targetDirs[0];
		Content content = ContentQuery.me().findBySlug(StringUtils.urlDecode(slug));
		if (null != content && Consts.MODULE_PAGE.equals(content.getModule())) {
			return Consts.ROUTER_CONTENT + SLASH + slug;
		}

		return null;
	}

}

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

import com.abreaking.core.render.freemarker.JTag;
import com.abreaking.model.Content;
import com.abreaking.model.query.ContentQuery;
import com.abreaking.common.util.StringUtils;

import java.math.BigInteger;

public class ContentTag extends JTag {
	
	public static final String TAG_NAME = "jp.content";

	@Override
	public void onRender() {

		BigInteger id = getParamToBigInteger("id");
		String slug = getParam("slug");

		if (id == null && StringUtils.isBlank(slug)) {
			renderText("");
			return;
		}

		Content content = null;
		if (id != null) {
			content = ContentQuery.me().findById(id);
		}

		if (content == null && StringUtils.isNotBlank(slug)) {
			content = ContentQuery.me().findBySlug(slug);
		}

		if (content == null) {
			renderText("");
			return;
		}

		setVariable("content", content);
		renderBody();
	}

}

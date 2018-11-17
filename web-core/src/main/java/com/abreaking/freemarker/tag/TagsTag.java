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

import com.abreaking.common.consts.Consts;
import com.abreaking.core.render.freemarker.JTag;
import com.abreaking.model.Taxonomy;
import com.abreaking.model.query.TaxonomyQuery;

import java.util.List;

public class TagsTag extends JTag {
	public static final String TAG_NAME = "jp.tags";

	@Override
	public void onRender() {

		int count = getParamToInt("count", 0);
		count = count <= 0 ? 10 : count;

		String orderby = getParam("orderBy");

		String module = getParam("module", Consts.MODULE_ARTICLE);
		List<Taxonomy> list = TaxonomyQuery.me().findListByModuleAndType(module, "tag", orderby, null, count);
		setVariable("tags", list);

		renderBody();
	}

}

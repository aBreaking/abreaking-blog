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
package com.abreaking;

import com.abreaking.model.query.OptionQuery;
import com.abreaking.search.api.SearcherFactory;
import com.jfinal.kit.PropKit;
import com.abreaking.core.Jpress;
import com.abreaking.core.JpressConfig;
import com.abreaking.db.api.DbDialectFactory;
import com.abreaking.message.Actions;
import com.abreaking.message.MessageKit;
import com.abreaking.freemarker.function.OptionChecked;
import com.abreaking.freemarker.function.OptionValue;
import com.abreaking.freemarker.function.TaxonomyBox;
import com.abreaking.freemarker.tag.*;
import com.abreaking.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Config extends JpressConfig {

	private static Map<String,String> webConfig = new HashMap<>();

	@Override
	public void onJfinalStartBefore() {
		dbDialectConfig();
	}

	@Override
	public void onJfinalStartAfter() {

		Jpress.addTag(ContentsTag.TAG_NAME, new ContentsTag());
		Jpress.addTag(ContentTag.TAG_NAME, new ContentTag());
		Jpress.addTag(ModulesTag.TAG_NAME, new ModulesTag());
		Jpress.addTag(TagsTag.TAG_NAME, new TagsTag());
		Jpress.addTag(TaxonomyTag.TAG_NAME, new TaxonomyTag());
		Jpress.addTag(TaxonomysTag.TAG_NAME, new TaxonomysTag());
		Jpress.addTag(ArchivesTag.TAG_NAME, new ArchivesTag());
		Jpress.addTag(UsersTag.TAG_NAME, new UsersTag());

		//add by liwei
		Jpress.addTag(RecommendContentTag.TAG_NAME,new RecommendContentTag());
		Jpress.addTag(MessagePageTag.TAG_NAME,new MessagePageTag());
		Jpress.addTag(VisitorPageTag.TAG_NAME,new VisitorPageTag());

		Jpress.addFunction("TAXONOMY_BOX", new TaxonomyBox());
		Jpress.addFunction("OPTION", new OptionValue());
		Jpress.addFunction("OPTION_CHECKED", new OptionChecked());

		doSearcherConfig();
		MessageKit.sendMessage(Actions.JPRESS_STARTED);
		//先读取下web的一些配置
		readWebConfig();
	}

	private void doSearcherConfig() {
		if (!Jpress.isInstalled()) {
			return;
		}
		String searcher = PropKit.get("jpress_searcher");
		if (StringUtils.isNotBlank(searcher)) {
			SearcherFactory.use(searcher);
		}
	}

	private void dbDialectConfig() {
		String dialect = PropKit.get("jpress_db_dialect");
		if (StringUtils.isNotBlank(dialect)) {
			DbDialectFactory.use(dialect);
		}
	}

	public void readWebConfig(){
		OptionQuery optionQuery = OptionQuery.me();
		webConfig.put("web_domain",optionQuery.findValue("web_domain"));
		webConfig.put("web_ip",optionQuery.findValue("web_ip"));
		webConfig.put("web_title",optionQuery.findValue("web_title"));
	}

	public static String getWebConfig(String key){
		return webConfig.get(key);
	}


}

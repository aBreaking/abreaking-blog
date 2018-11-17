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
package com.abreaking.admin.controller;

import com.jfinal.aop.Before;
import com.abreaking.core.JBaseController;
import com.abreaking.core.interceptor.ActionCacheClearInterceptor;
import com.abreaking.interceptor.UCodeInterceptor;
import com.abreaking.message.Actions;
import com.abreaking.message.MessageKit;
import com.abreaking.model.query.OptionQuery;
import com.abreaking.router.RouterMapping;
import com.abreaking.router.RouterNotAllowConvert;
import com.abreaking.common.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@RouterMapping(url = "/admin/option", viewPath = "/WEB-INF/admin/option")
@Before(ActionCacheClearInterceptor.class)
@RouterNotAllowConvert
public class _OptionController extends JBaseController {

	public void index() {
		render((getPara() == null ? "web" : getPara()) + ".html");
	}

	@Before(UCodeInterceptor.class)
	public void save() {

		HashMap<String, String> filesMap = getUploadFilesMap();

		HashMap<String, String> datasMap = new HashMap<String, String>();

		Map<String, String[]> paraMap = getParaMap();
		if (paraMap != null && !paraMap.isEmpty()) {
			for (Map.Entry<String, String[]> entry : paraMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().length > 0) {
					String value = null;
					for (String v : entry.getValue()) {
						if (StringUtils.isNotEmpty(v)) {
							value = v;
							break;
						}
					}
					datasMap.put(entry.getKey(), value);
				}
			}
		}

		String autosaveString = getPara("autosave");
		if (StringUtils.isNotBlank(autosaveString)) {
			String[] keys = autosaveString.split(",");
			for (String key : keys) {
				if (StringUtils.isNotBlank(key)) {
					datasMap.put(key.trim(), getRequest().getParameter(key.trim()));
				}
			}
		}

		for (Map.Entry<String, String> entry : datasMap.entrySet()) {
			doSave(entry.getKey(), entry.getValue(), filesMap);
		}

		MessageKit.sendMessage(Actions.SETTING_CHANGED, datasMap);
		renderAjaxResultForSuccess();
	}

	private void doSave(String key, String value, HashMap<String, String> filesMap) {

		if (filesMap != null && filesMap.containsKey(key)) {
			// 有相同的key的情况下，以上传的文件为准。
			value = filesMap.get(key);
		}

		if ("".equals(value)) {
			value = null;
		}

		OptionQuery.me().saveOrUpdate(key, value);
	}

}
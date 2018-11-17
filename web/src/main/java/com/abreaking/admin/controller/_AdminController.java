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
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Page;
import com.abreaking.common.consts.Consts;
import com.abreaking.core.JBaseController;
import com.abreaking.core.interceptor.ActionCacheClearInterceptor;
import com.abreaking.interceptor.AdminInterceptor;
import com.abreaking.interceptor.UCodeInterceptor;
import com.abreaking.message.Actions;
import com.abreaking.message.MessageKit;
import com.abreaking.model.Comment;
import com.abreaking.model.Content;
import com.abreaking.model.User;
import com.abreaking.model.query.CommentQuery;
import com.abreaking.model.query.ContentQuery;
import com.abreaking.model.query.UserQuery;
import com.abreaking.router.RouterMapping;
import com.abreaking.router.RouterNotAllowConvert;
import com.abreaking.template.TemplateManager;
import com.abreaking.template.TplModule;
import com.abreaking.common.util.CookieUtils;
import com.abreaking.common.util.EncryptUtils;
import com.abreaking.common.util.StringUtils;

import java.util.List;

@RouterMapping(url = "/admin", viewPath = "/WEB-INF/admin")
@RouterNotAllowConvert
public class _AdminController extends JBaseController {

	@Before(ActionCacheClearInterceptor.class)
	public void index() {
		
		List<TplModule> moduleList = TemplateManager.me().currentTemplateModules();
		setAttr("modules", moduleList);

		if (moduleList != null && moduleList.size() > 0) {
			String moduels[] = new String[moduleList.size()];
			for (int i = 0; i < moduleList.size(); i++) {
				moduels[i] = moduleList.get(i).getName();
			}

			List<Content> contents = ContentQuery.me().findListInNormal(1, 20, null, null, null, null, moduels, null,
					null, null, null, null, null, null, null);
			setAttr("contents", contents);
		}

		Page<Comment> commentPage = CommentQuery.me().paginateWithContentNotInDelete(1, 10, null, null, null, null);
		if (commentPage != null) {
			setAttr("comments", commentPage.getList());
		}

		render("index.html");
	}

	@Clear(AdminInterceptor.class)
	public void login() {
		String username = getPara("username");
		String password = getPara("password");

		if (!StringUtils.areNotEmpty(username, password)) {
			render("login.html");
			return;
		}

		User user = UserQuery.me().findUserByUsername(username);

		if (null == user) {
			renderAjaxResultForError("没有该用户");
			return;
		}

		if (EncryptUtils.verlifyUser(user.getPassword(), user.getSalt(), password) && user.isAdministrator()) {

			MessageKit.sendMessage(Actions.USER_LOGINED, user);

			CookieUtils.put(this, Consts.COOKIE_LOGINED_USER, user.getId().toString());

			renderAjaxResultForSuccess("登陆成功");
		} else {
			renderAjaxResultForError("密码错误");
		}
	}

	@Before(UCodeInterceptor.class)
	public void logout() {
		CookieUtils.remove(this, Consts.COOKIE_LOGINED_USER);
		redirect("/admin");
	}

}

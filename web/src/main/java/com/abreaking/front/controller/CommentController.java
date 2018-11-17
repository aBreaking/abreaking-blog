/**
 * Copyright (c) 2018 abreaking.com by liwei , learn from jpress of yangfuhai
 */
package com.abreaking.front.controller;

import com.abreaking.common.consts.Consts;
import com.abreaking.core.BaseFrontController;
import com.abreaking.core.cache.ActionCacheManager;
import com.abreaking.message.Actions;
import com.abreaking.message.MessageKit;
import com.abreaking.model.Comment;
import com.abreaking.model.Content;
import com.abreaking.model.query.ContentQuery;
import com.abreaking.model.query.OptionQuery;
import com.abreaking.model.query.UserQuery;
import com.abreaking.router.RouterMapping;
import com.abreaking.common.util.StringUtils;

import java.math.BigInteger;
import java.util.Date;

@RouterMapping(url = "/comment")
public class CommentController extends BaseFrontController {

	//一些敏感词就先带审核
	public final static String[] TEXT_SENSITIVE_WORD = new String[]{"草泥马","草你","你妈","日你","傻逼","sb"
			,"傻缺","妈卖","卖批","屌","傻吊","狗逼","狗比","fuck"};
	public final static String[] AUTHOR_SENSITIVE_WORD = new String[]{"(博主)","（博主）","（博主)","(博主)"};
	//防止XSS注入，script标签好像已经自带忽视了
	public final static String XSS_SENSITIVE_CODE = ".*<[a-zA-Z0-9]+\\s+[a-zA-Z]+.*";

	public final static String MY_KEY = "5172551";
	public final static String MY_CODE = "liwei";

	public void index() {
		renderError(404);
	}

	public void submit() {
		String author = getPara("author");
		String email = getPara("email");
		String text = getPara("text");
		String ip = getIPAddress();
		String agent = getUserAgent();
		String gotoUrl = getPara("goto");
		String anchor = getPara("anchor");
		BigInteger userId = StringUtils.toBigInteger(getPara("user_id"),null);//
		String type = Comment.TYPE_COMMENT;
		String status = Comment.STATUS_NORMAL;
		if (gotoUrl == null) {
			gotoUrl = getRequest().getHeader("Referer");
		}

		if (gotoUrl != null && anchor != null) {
			gotoUrl += "#" + anchor;
		}

		// 是否开启验证码功能
		Boolean comment_need_captcha = OptionQuery.me().findValueAsBool("comment_need_captcha");
		if (comment_need_captcha != null && comment_need_captcha == true) {
			if (!validateCaptcha("comment_captcha")) { // 验证码验证失败
				renderForCommentError("validate captcha fail", 1);
				return;
			}
		}

		//TODO 以后开启登陆功能再打开这个
		//BigInteger userId = StringUtils.toBigInteger(CookieUtils.get(this, Consts.COOKIE_LOGINED_USER), null);

		// 允许未登陆用户评论
		Boolean comment_allow_not_login = OptionQuery.me().findValueAsBool("comment_allow_not_login");

		// 允许未登陆用户评论
		if (comment_allow_not_login == null || comment_allow_not_login == false) {
			if (userId == null) {
				String redirect = Consts.ROUTER_USER_LOGIN;
				if (StringUtils.isNotBlank(gotoUrl)) {
					redirect += "?goto=" + StringUtils.urlEncode(gotoUrl);
				}
				redirect(redirect);
				return;
			}
		}

		//回复内容的敏感词控制
		if(checkSensitive(author,AUTHOR_SENSITIVE_WORD)||checkSensitive(author,TEXT_SENSITIVE_WORD)||
				checkXSS(author,XSS_SENSITIVE_CODE)||checkSensitive(text,TEXT_SENSITIVE_WORD)
				||checkXSS(text,XSS_SENSITIVE_CODE)){
			status = Comment.STATUS_DRAFT;
			//TODO 给前台反馈错误信息
		}else{
			Boolean comment_must_audited = OptionQuery.me().findValueAsBool("comment_must_audited");
			/*if (comment_must_audited != null && comment_must_audited) {
				status = Comment.STATUS_DRAFT;
			}*/
		}

		BigInteger contentId = getParaToBigInteger("cid");
		if (contentId == null) {
			renderForCommentError("comment fail,content id is null.", 1);
			return;
		}

		Content content = ContentQuery.me().findById(contentId);
		if (content == null) {
			renderForCommentError("find not find the content, maybe it has bean deleted.", 1);
			return;
		}

		if (!content.isCommentEnable()) {
			renderForCommentError("the comment function of the content has been closed.", 1);
			return;
		}


		if (StringUtils.isBlank(text)) {
			renderForCommentError("comment fail,text is blank.", 2);
			return;
		}



		/*
		if (userId != null) {
			User user = UserQuery.me().findById(userId);
			if (user != null) {
				author = StringUtils.isNotBlank(user.getNickname()) ? user.getNickname() : user.getUsername();
			}
		}*/

		//TODO 这里放一个我本人的标识，对未表明的身份的网友应该用ip去标识
		UserQuery userQuery = new UserQuery();
		if (StringUtils.isBlank(author)) {
			String defautAuthor = OptionQuery.me().findValue("comment_default_nickname");
			author = StringUtils.isNotBlank(defautAuthor) ? defautAuthor : userQuery.findById(new BigInteger("99999")).getNickname();
		}else if(MY_KEY.equals(author)){
			userId = new BigInteger("1");	//我的头像
			author = userQuery.findById(userId).getNickname()+"（博主）";	//我的代号
		}

		BigInteger parentId = getParaToBigInteger("parent_id");

		Comment comment = new Comment();
		comment.setContentModule(content.getModule());
		comment.setType(Comment.TYPE_COMMENT);
		comment.setContentId(content.getId());
		comment.setText(text);
		comment.setIp(ip);
		comment.setAgent(agent);
		comment.setAuthor(author);
		comment.setEmail(email);
		comment.setType(type);
		comment.setStatus(status);
		comment.setUserId(userId);
		comment.setCreated(new Date());
		comment.setParentId(parentId);

		if (comment.save()) {
			MessageKit.sendMessage(Actions.COMMENT_ADD, comment);
			ActionCacheManager.clearCache();
		}

		if (isAjaxRequest()) {
			renderAjaxResultForSuccess();
			return;
		}

		if (gotoUrl != null) {
			redirect(gotoUrl);
			return;
		}

		renderText("comment ok");

	}

	private void renderForCommentError(String message, int errorCode) {
		String referer = getRequest().getHeader("Referer");
		if (isAjaxRequest()) {
			renderAjaxResult(message, errorCode);
		} else {
			redirect(referer + "#" + getPara("anchor"));
		}
	}

	public static boolean checkSensitive(String text,String[] scope){

		for(String s : scope){
			if(StringUtils.areNotBlank(text)&&text.indexOf(s)!=-1){
				return true;
			}
		}
		return false;
	}

	public static boolean checkXSS(String text,String regex){
		if(StringUtils.areNotBlank(text)){
			return text.matches(regex);
		}
		return false;
	}


}

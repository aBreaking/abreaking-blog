package com.abreaking.front.controller;

import com.abreaking.common.consts.Consts;
import com.abreaking.core.BaseFrontController;
import com.abreaking.core.cache.ActionCacheManager;
import com.abreaking.message.Actions;
import com.abreaking.message.MessageKit;
import com.abreaking.model.Message;
import com.abreaking.model.query.OptionQuery;
import com.abreaking.model.query.UserQuery;
import com.abreaking.router.RouterMapping;
import com.abreaking.common.util.StringUtils;

import java.math.BigInteger;
import java.util.Date;

@RouterMapping(url = "/message")
public class MessageController extends BaseFrontController {


    //一些敏感词就先带审核
    private final static String[] TEXT_SENSITIVE_WORD = CommentController.TEXT_SENSITIVE_WORD;
    private final static String[] AUTHOR_SENSITIVE_WORD = CommentController.AUTHOR_SENSITIVE_WORD;
    //防止XSS注入，script标签好像已经自带忽视了
    private final static String XSS_SENSITIVE_CODE = CommentController.XSS_SENSITIVE_CODE;

    private final static String MY_KEY = CommentController.MY_KEY;
    private final static String MY_CODE = CommentController.MY_CODE;
    public void index() {

        render("message.html");
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
        String type = Message.TYPE_MESSAGE;
        String status = Message.STATUS_NORMAL;
        if (gotoUrl == null) {
            gotoUrl = getRequest().getHeader("Referer");
        }

        if (gotoUrl != null && anchor != null) {
            gotoUrl += "#" + anchor;
        }


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
            status = Message.STATUS_DRAFT;
            //TODO 给前台反馈错误信息
        }else{
            Boolean comment_must_audited = OptionQuery.me().findValueAsBool("comment_must_audited");
            if (comment_must_audited != null && comment_must_audited) {
                status = Message.STATUS_DRAFT;
            }
        }

        if (StringUtils.isBlank(text)) {
            renderForCommentError("Message fail,text is blank.", 2);
            return;
        }



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

        Message message = new Message();

        message.setType(Message.TYPE_MESSAGE);
        message.setText(text);
        message.setIp(ip);
        message.setAgent(agent);
        message.setAuthor(author);
        message.setEmail(email);
        message.setType(type);
        message.setStatus(status);
        message.setUserId(userId);
        message.setCreated(new Date());
        message.setParentId(parentId);

        //该留言保存
        if (message.save()) {
            MessageKit.sendMessage(Actions.COMMENT_ADD, message);
            ActionCacheManager.clearCache();
        }
        //更新父留言 的子留言数量
        if(parentId!=null){
            Message parentMessage = message.findById(parentId);
            Integer sonNumber = parentMessage.getSonNumber();
            sonNumber = sonNumber==null?0:sonNumber;
            parentMessage.setSonNumber(sonNumber+1);
            if(parentMessage.update()){
                MessageKit.sendMessage(Actions.COMMENT_ADD, message);
                ActionCacheManager.clearCache();
            }
        }

        if (isAjaxRequest()) {
            renderAjaxResultForSuccess();
            return;
        }

        if (gotoUrl != null) {
            redirect(gotoUrl);
            return;
        }

        renderText("Message ok");

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

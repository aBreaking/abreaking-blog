package com.abreaking.freemarker.tag;

import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;
import com.abreaking.core.render.freemarker.BasePaginateTag;
import com.abreaking.core.render.freemarker.JTag;
import com.abreaking.model.Message;
import com.abreaking.model.query.MessageQuery;
import com.abreaking.common.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

public class MessagePageTag extends JTag {
    public static final String TAG_NAME = "jp.messagePage";

    int pageNumber;
    HttpServletRequest request;

    public MessagePageTag(HttpServletRequest request, int pageNumbqcer) {
        this.request = request;
        this.pageNumber = pageNumbqcer;
    }

    public MessagePageTag(){}

    @Override
    public void onRender() {

        String parentId = getParam("pId");
        int pn = getParamToInt("pageNumber",1);
        int pageSize = getParamToInt("pageSize", 10);
        Page<Message> page = null;
        if(StringUtils.isNotBlank(parentId)){ //查该爹下的所有儿子
            page = MessageQuery.me().paginatewithSon(pn,pageSize,new BigInteger(parentId));
        }else{  //查所有爹
            page = MessageQuery.me().paginatewithNoParent(pn,pageSize);
        }
        setVariable("page", page);
        setVariable("messages", page.getList());

        MessagePageTag.MessagePaginateTag pagination = new MessagePageTag.MessagePaginateTag(request, page);

        setVariable("pagination", pagination);

        renderBody();
    }

    public static class MessagePaginateTag extends BasePaginateTag {

        final HttpServletRequest request;

        public MessagePaginateTag(HttpServletRequest request, Page<Message> page) {
            super(page);
            this.request = request;
        }

        @Override
        protected String getUrl(int pageNumber) {
            String url = "/message";

            /*String queryString = request.getQueryString();
            if (StringUtils.isNotBlank(queryString)) {
                url += "?" + queryString;
            }*/

            if (StringUtils.isNotBlank(getAnchor())) {
                url += "#" + getAnchor();
            }
            return JFinal.me().getContextPath() + url;
        }

    }
}

package com.abreaking.freemarker.tag;

import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Page;
import com.abreaking.core.render.freemarker.BasePaginateTag;
import com.abreaking.core.render.freemarker.JTag;
import com.abreaking.model.Visitor;
import com.abreaking.model.query.VisitorQuery;
import com.abreaking.common.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class VisitorPageTag extends JTag {
    public static final String TAG_NAME = "jp.visitorPage";

    HttpServletRequest request;

    @Override
    public void onRender() {
        int pn = getParamToInt("pageNumber",1);
        int pageSize = getParamToInt("pageSize", 10);

        Page<Visitor> page = VisitorQuery.me().paginateByVisitTimeDesc(pn, pageSize);
        setVariable("page", page);

        VisitorPageTag.VisitorPaginateTag pagination = new VisitorPageTag.VisitorPaginateTag(request, page);

        setVariable("pagination", pagination);

        renderBody();

    }


    public static class VisitorPaginateTag extends BasePaginateTag {

        final HttpServletRequest request;

        public VisitorPaginateTag(HttpServletRequest request, Page<Visitor> page) {
            super(page);
            this.request = request;
        }

        @Override
        protected String getUrl(int pageNumber) {
            String url = "/admin";

            if (StringUtils.isNotBlank(getAnchor())) {
                url += "#" + getAnchor();
            }
            return JFinal.me().getContextPath() + url;
        }

    }
}

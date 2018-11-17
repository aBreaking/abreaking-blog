package com.abreaking.filter;

import com.abreaking.Config;
import com.abreaking.common.util.StringUtils;
import com.abreaking.model.query.OptionQuery;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CrossOriginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String webDomain = Config.getWebConfig("web_domain");

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Access-Control-Allow-Methods", "get,post,options");

        response.setHeader("Access-Control-Allow-Origin", "http://"+webDomain);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}

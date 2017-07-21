package com.runoob.cas.client.filter;

import com.alibaba.fastjson.JSONObject;
import com.runoob.cas.client.autoconfig.CasProperties;
import org.jasig.cas.client.util.AbstractCasFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 赵秀非 E-mail:zhaoxiufei@gmail.com
 * @version 创建时间：2017/7/12 9:20
 */
public class SessionTimeOutFilter implements Filter {
    private CasProperties casProperties;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (!casProperties.getSessionTimeOutIgnoreUrls().contains(request.getRequestURI())) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            if (request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) == null) {
                ajaxRequest(response);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private void ajaxRequest(HttpServletResponse servletResponse) throws IOException {
        servletResponse.setHeader("Session-Status", "Session-Out");   //在响应头设置session状态
        servletResponse.setHeader("Redirect-Url", casProperties.getServerName());//在响应头设置跳转URL
        servletResponse.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = servletResponse.getWriter()) {
            JSONObject jo = new JSONObject();//TODO 可修改这里
            jo.put("code", 301);
            jo.put("msg", "登录超时,请重新登录");
            jo.put("data", casProperties.getServerName());
            out.println(jo);
        }
    }

    public void setCasProperties(CasProperties casProperties) {
        this.casProperties = casProperties;
    }
}

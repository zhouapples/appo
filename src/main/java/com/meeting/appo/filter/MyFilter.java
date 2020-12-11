package com.meeting.appo.filter;


import javax.servlet.*;
import java.io.IOException;


public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("filter init.....");
    }

    @Override
    public void destroy() {
        System.out.println("filter destroy.....");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter is working....");
        filterChain.doFilter(servletRequest,servletResponse);
    }
}

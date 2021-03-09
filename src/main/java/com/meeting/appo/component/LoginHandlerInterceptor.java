package com.meeting.appo.component;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在目标方法执行之前
        Object user = request.getSession().getAttribute("loginUser");
        if (user == null){
            //登陆检测，如未登录返回登陆界面
            request.setAttribute("msg","没有访问权限，请先登录");

            //获取转发器将页面重定向至登录页
            request.getRequestDispatcher("/login").forward(request,response);
            return false;
        }else{
            //已经登陆的话，放行
             return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

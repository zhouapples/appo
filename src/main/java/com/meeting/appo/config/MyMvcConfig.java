package com.meeting.appo.config;

import com.meeting.appo.component.LoginHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置拦截器放行的资源路径
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns(
                "/",
                "/detail",
                "/login",
                "/regist",
                "/druid/*",
                "/*/*.js",
                "/*/*.css",
                "/*/*.otf",
                "/*/*.eof",
                "/*/*.svg",
                "/*/*.ttf",
                "/*/*.woff",
                "/*/*.woff2",
                "/*/*.less",
                "/*/*.scss",
                "/*/*.eot"

        );
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/druid").setViewName("druid/login.html");  //druid监控页面
    }
}

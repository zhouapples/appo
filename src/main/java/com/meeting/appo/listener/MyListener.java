package com.meeting.appo.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("myListener--contextInitialized,web 项目启动");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("myListener--contextInitialized,web 项目销毁");
    }
}

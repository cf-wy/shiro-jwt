package com.shiro.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.shiro.demo.dao")
public class DemoApplication{

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /*@Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("shiroEnvironmentClass","org.apache.shiro.web.env.IniWebEnvironment");
        //servletContext.setInitParameter("shiroConfigLocations","classpath:shiro.ini");//基于authc
        //servletContext.setInitParameter("shiroConfigLocations","classpath:shiro-basicfilterlogin.ini");//基于authcBasic
        servletContext.setInitParameter("shiroConfigLocations","classpath:shiro-formfilterlogin.ini");//基于表单form
    }*/
}

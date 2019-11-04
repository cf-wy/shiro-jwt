/*
package com.shiro.demo.config;

import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;

*/
/**
 * 之前是iniShiroFilter
 * shiro1.2以后版本引入Environment/WebEnvironment 的概念，即由它们的实现提供相应的 SecurityManager 及其相应的依赖。ShiroFilter 会自动找到 Environment 然后获取相应的依赖。
 *//*

@SpringBootConfiguration
public class ShiroFilterConfig {
    @Bean
    public FilterRegistrationBean<ShiroFilter> registrationBean(){
        FilterRegistrationBean<ShiroFilter> filterFilterRegistrationBean=new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new ShiroFilter());
        filterFilterRegistrationBean.setName("shiroFilter");
        return filterFilterRegistrationBean;
    }
    @Bean
    public ServletListenerRegistrationBean<EnvironmentLoaderListener> listenerServletListenerRegistrationBean(){
        ServletListenerRegistrationBean<EnvironmentLoaderListener> servletListenerRegistrationBean=new ServletListenerRegistrationBean<>();
        servletListenerRegistrationBean.setListener(new EnvironmentLoaderListener());
        return servletListenerRegistrationBean;
    }
}
*/

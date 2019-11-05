package com.shiro.demo.config;

import com.shiro.demo.filters.JwtAuthFilter;
import com.shiro.demo.realm.DbShiroRealm;
import com.shiro.demo.realm.JWTShiroRealm;
import com.shiro.demo.service.UserService;
import com.shiro.demo.util.JwtUtil;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootConfiguration
public class FilterConfig {
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String,String> paths=new LinkedHashMap<>();
        paths.put("/favicon.ico","anon");
        paths.put("/unauthorized","anon");
        paths.put("/static/**","anon");
        paths.put("/index","noSessionCreation,user");
        paths.put("/login","noSessionCreation,anon");
        paths.put("/logout","noSessionCreation,authcToken[permissive]");
        paths.put("/**","noSessionCreation,authcToken");
        chainDefinition.addPathDefinitions(paths);
        return chainDefinition;
    }
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, UserService userService, Environment environment) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setLoginUrl(environment.getProperty("shiro.loginUrl"));
        factoryBean.setSuccessUrl(environment.getProperty("shiro.successUrl"));
        factoryBean.setUnauthorizedUrl(environment.getProperty("shiro.unauthorizedUrl"));
        factoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filterMap = factoryBean.getFilters();
        filterMap.put("authcToken", new JwtAuthFilter(userService));
        //filterMap.put("anyRole", createRolesFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        return factoryBean;
    }
    @Bean
    public Authenticator authenticator(@Qualifier("dbRealm")Realm dbRealm,@Qualifier("jwtRealm")Realm jwtRealm) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        //设置两个Realm，一个用于用户登录验证和访问权限获取；一个用于jwt token的认证
        authenticator.setRealms(Arrays.asList(dbRealm,jwtRealm));
        //设置多个realm认证策略，一个成功即跳过其它的
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证。
     * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session，如果要完全禁用，要配合下面的noSessionCreation的Filter来实现
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }
    /**
     * 用于用户名密码登录时认证的realm
     */
    @Bean("dbRealm")
    public Realm dbShiroRealm(UserService userService) {
        DbShiroRealm dbShiroRealm = new DbShiroRealm();
        dbShiroRealm.setUserService(userService);
        dbShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return dbShiroRealm;
    }
    /**
     * 用于JWT token认证的realm
     */
    @Bean("jwtRealm")
    public Realm jwtShiroRealm(JwtUtil jwtUtil, UserService userService) {
        JWTShiroRealm jwtShiroRealm = new JWTShiroRealm();
        jwtShiroRealm.setJwtUtil(jwtUtil);
        jwtShiroRealm.setUserService(userService);
        return jwtShiroRealm;
    }
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher matcher=new HashedCredentialsMatcher();
        matcher.setStoredCredentialsHexEncoded(true);
        matcher.setHashIterations(2);
        matcher.setHashAlgorithmName("md5");
        return matcher;
    }
}

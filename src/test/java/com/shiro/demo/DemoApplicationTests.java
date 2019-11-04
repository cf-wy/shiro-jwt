package com.shiro.demo;

import com.shiro.demo.credentials.RetryLimitHashedCredentialsMatcher;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.AbstractConverter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testHelloworld() {
        BeanUtilsBean.getInstance().getConvertUtils().register(new EnumConverter(), JdbcRealm.SaltStyle.class);
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-hashedCredentialsMatcher.ini");
        SecurityManager securityManager = factory.getInstance();
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("wang", "123");
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
        }
        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }
    private class EnumConverter extends AbstractConverter {
        @Override
        protected String convertToString(final Object value) throws Throwable {
            return ((Enum) value).name();
        }
        @Override
        protected Object convertToType(final Class type, final Object value) throws Throwable {
            return Enum.valueOf(type, value.toString());
        }

        @Override
        protected Class getDefaultType() {
            return null;
        }

    }

    private void login(String configFile) {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory(configFile);
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        subject.login(token);
    }
    @Test
    public void testAllSuccessfulStrategyWithSuccess() {
        login("classpath:shiro-authenticator-all-success.ini");
        Subject subject = SecurityUtils.getSubject();
        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principalCollection = subject.getPrincipals();
        log.info(principalCollection.toString());
        Assert.assertEquals(2, principalCollection.asList().size());
    }

    @Test
    public void testHasRole() {
        login("classpath:shiro-role.ini");
        Subject subject = SecurityUtils.getSubject();
        //判断拥有角色：role1
        Assert.assertTrue(subject.hasRole("role1"));
        //判断拥有角色：role1 and role2
        Assert.assertTrue(subject.hasAllRoles(Arrays.asList("role1", "role2")));
        //判断拥有角色：role1 and role2 and !role3
        boolean[] result = subject.hasRoles(Arrays.asList("role1", "role2", "role3"));
        Assert.assertEquals(true, result[0]);
        Assert.assertEquals(true, result[1]);
        Assert.assertEquals(false, result[2]);
    }
    @Test(expected = UnauthorizedException.class)
    public void testCheckRole() {
        login("classpath:shiro-role.ini");
        Subject subject = SecurityUtils.getSubject();
        //断言拥有角色：role1
        subject.checkRole("role1");
        //断言拥有角色：role1 and role3 失败抛出异常
        subject.checkRoles("role1", "role3");
    }
    @Test
    public void testIsPermitted() {
        login("classpath:shiro-permission.ini");
        Subject subject = SecurityUtils.getSubject();
        //判断拥有权限：user:create
        Assert.assertTrue(subject.isPermitted("user:create"));
        //判断拥有权限：user:update and user:delete
        Assert.assertTrue(subject.isPermittedAll("user:update", "user:delete"));
        //判断没有权限：user:view
        Assert.assertFalse(subject.isPermitted("user:view"));
    }
    @Test(expected = UnauthorizedException.class)
    public void testCheckPermission () {
        login("classpath:shiro-permission.ini");
        Subject subject = SecurityUtils.getSubject();
        //断言拥有权限：user:create
        subject.checkPermission("user:create");
        //断言拥有权限：user:delete and user:update
        subject.checkPermissions("user:delete", "user:update");
        //断言拥有权限：user:view 失败抛出异常
        subject.checkPermissions("user:view");
    }

    @Test
    public void testHashCredentialMatcher(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        HashedCredentialsMatcher matcher=new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(2);
        matcher.setStoredCredentialsHexEncoded(true);
        //设置Realm
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/shiro?useUnicode=true&characterEncoding=utf-8");
        ds.setUsername("root");
        ds.setPassword("123456");
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(ds);
        jdbcRealm.setPermissionsLookupEnabled(true);
        jdbcRealm.setSaltStyle(JdbcRealm.SaltStyle.COLUMN);
        jdbcRealm.setAuthenticationQuery("select password, concat(username,password_salt) from users where username = ?");
        jdbcRealm.setCredentialsMatcher(matcher);
        securityManager.setRealms(Arrays.asList((Realm) jdbcRealm));
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("wang", "123");
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
        }
        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }
    @Test
    public void testRetryHashCredentialMatcher(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        RetryLimitHashedCredentialsMatcher matcher=new RetryLimitHashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(2);
        matcher.setStoredCredentialsHexEncoded(true);
        //设置Realm
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setJdbcUrl("jdbc:mysql://localhost:3306/shiro?useUnicode=true&characterEncoding=utf-8");
        ds.setUsername("root");
        ds.setPassword("123456");
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(ds);
        jdbcRealm.setPermissionsLookupEnabled(true);
        jdbcRealm.setSaltStyle(JdbcRealm.SaltStyle.COLUMN);
        jdbcRealm.setAuthenticationQuery("select password, concat(username,password_salt) from users where username = ?");
        jdbcRealm.setCredentialsMatcher(matcher);
        securityManager.setRealms(Arrays.asList((Realm) jdbcRealm));
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("wang", "1234");
        for (int i=1;i<=5;i++){

        try {
            subject.login(token);
            //4、登录，即身份验证
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
        }
        }
        subject.login(token);
        //Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        //subject.logout();
    }
}

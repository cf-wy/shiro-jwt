package com.shiro.demo;

import com.shiro.demo.credentials.RetryLimitHashedCredentialsMatcher;
import com.shiro.demo.enums.UserStatus;
import com.shiro.demo.model.Permission;
import com.shiro.demo.model.Role;
import com.shiro.demo.model.User;
import com.shiro.demo.realm.UserRealm;
import com.shiro.demo.service.PermissionService;
import com.shiro.demo.service.RoleService;
import com.shiro.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ServiceTest {
    @Autowired
    protected PermissionService permissionService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected UserService userService;


    @Test
    public void setUp() {
        roleService.deleteAll();
        permissionService.deleteAll();
        userService.deleteAll();
        //2、新增角色
        Role r1 = new Role("admin", "管理员", Boolean.TRUE);
        Role r2 = new Role("user", "用户管理员", Boolean.TRUE);
        roleService.createRole(r1);
        roleService.createRole(r2);
        //1、新增权限
        Permission p1 = new Permission("user:create", "用户模块新增", Boolean.TRUE);
        Permission p2 = new Permission("user:update", "用户模块修改", Boolean.TRUE);
        Permission p3 = new Permission("menu:create", "菜单模块新增", Boolean.TRUE);
        permissionService.createPermission(p1);
        permissionService.createPermission(p2);
        permissionService.createPermission(p3);

        //4、新增用户
        String password="123";
        User u1 = new User("zhang", password);
        User u2 = new User("li", password);
        User u3 = new User("wu", password);
        User u4 = new User("wang", password);
        u4.setLocked(UserStatus.LOCK.getKey().byteValue());
        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);
        userService.createUser(u4);
        Set<User> users=new HashSet<>();
        users.add(u1);users.add(u2);
        //r1.setUsers(users);
        Set<Permission> permissions=new HashSet<>();
        permissions.add(p1);permissions.add(p3);
        //r1.setPermissions(permissions);
        roleService.updateRole(r1);
    }

    @After
    public void tearDown() throws Exception {
        ThreadContext.unbindSubject();//退出时请解除绑定Subject到线程 否则对下次测试造成影响
    }

    @Test
    public void testUserRolePermissionRelation() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        RetryLimitHashedCredentialsMatcher matcher=new RetryLimitHashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(2);
        matcher.setStoredCredentialsHexEncoded(true);
        UserRealm userRealm=new UserRealm();
        userRealm.setUserService(userService);
        userRealm.setCredentialsMatcher(matcher);
        securityManager.setRealms(Arrays.asList((Realm) userRealm));
        SecurityUtils.setSecurityManager(securityManager);

        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("li", "123");
    /*for(int i=1;i<=5;i++){
        try{

            subject.login(token);
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }*/
        subject.login(token);
        log.info(subject.getPrincipal().toString());
        System.out.println(subject.isAuthenticated());
        System.out.println(subject.hasRole("admin"));
        System.out.println(subject.isPermitted("user:create"));
        subject.getSession();
    }
}

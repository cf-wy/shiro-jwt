package com.shiro.demo.realm;

import com.shiro.demo.filters.JwtToken;
import com.shiro.demo.model.User;
import com.shiro.demo.service.RoleService;
import com.shiro.demo.service.UserService;
import com.shiro.demo.util.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("————身份认证方法————");
        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = jwtUtil.getUsername(token);
        if (username == null ) {
            throw new AuthenticationException("token认证失败！");
        }
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new AuthenticationException("该用户不存在！");
        }
        if (user.getLocked()==1) {
            throw new AuthenticationException("该用户已被封号！");
        }
        return new SimpleAuthenticationInfo(token, token, "MyRealm");
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        /*System.out.println("————权限认证————");
        String username = JWTUtil.getUsername(principals.toString());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 此处最好使用缓存提升速度
        UserInfo userInfo = userInfoMapper.selectByName(username);
        userInfo = userInfoMapper.selectUserOfRole(userInfo.getUid());
        if (userInfo == null || userInfo.getRoleList().isEmpty()) {
            return authorizationInfo;
        }
        for (Role role : userInfo.getRoleList()) {
            authorizationInfo.addRole(role.getRole());
            role = roleMapper.selectRoleOfPerm(role.getId());
            if (role == null || role.getPermissions().isEmpty()) {
                continue;
            }
            for (Permission p : role.getPermissions()) {
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }
        return authorizationInfo;*/
        return null;
    }
}
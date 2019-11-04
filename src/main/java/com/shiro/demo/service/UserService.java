package com.shiro.demo.service;

import com.shiro.demo.model.User;

import java.util.Set;

public interface UserService {
     int createUser(User user); //创建账户
     void changePassword(Long userId, String newPassword);//修改密码
     void correlationRoles(Long userId, Long... roleIds); //添加用户-角色关系
     void uncorrelationRoles(Long userId, Long... roleIds);// 移除用户-角色关系
     User findByUsername(String username);// 根据用户名查找用户
     Set<String> findRoles(String username);// 根据用户名查找其角色
     Set<String> findPermissions(String username); //根据用户名查找其权限
    void deleteAll();
}

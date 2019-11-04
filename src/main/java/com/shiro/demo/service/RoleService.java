package com.shiro.demo.service;


import com.shiro.demo.model.Role;

public interface RoleService {
     int createRole(Role role);
     void deleteRole(Long roleId);
     int updateRole(Role role);
     void deleteAll();
    //添加角色-权限之间关系
     void correlationPermissions(Long roleId, Long... permissionIds);
    //移除角色-权限之间关系
     void uncorrelationPermissions(Long roleId, Long... permissionIds);
}

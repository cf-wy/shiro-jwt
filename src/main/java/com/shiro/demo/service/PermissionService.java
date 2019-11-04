package com.shiro.demo.service;


import com.shiro.demo.model.Permission;

public interface PermissionService {
     int createPermission(Permission permission);
     void deletePermission(Long permissionId);
     void deleteAll();
}

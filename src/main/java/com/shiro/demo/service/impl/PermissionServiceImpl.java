package com.shiro.demo.service.impl;

import com.shiro.demo.dao.PermissionDao;
import com.shiro.demo.model.Permission;
import com.shiro.demo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    @Override
    public int createPermission(Permission permission) {
        return permissionDao.insertSelective(permission);
    }

    @Override
    public void deletePermission(Long permissionId) {
        permissionDao.deleteByPrimaryKey(permissionId);
    }

    @Override
    public void deleteAll() {
        permissionDao.deleteAll();
    }
}

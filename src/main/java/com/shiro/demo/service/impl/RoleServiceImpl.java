package com.shiro.demo.service.impl;

import com.shiro.demo.dao.RoleDao;
import com.shiro.demo.model.Role;
import com.shiro.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Override
    public int createRole(Role role) {
        return roleDao.insertSelective(role);
    }
    @Override
    public int updateRole(Role role) {
        return roleDao.updateByPrimaryKeySelective(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        roleDao.deleteByPrimaryKey(roleId);
    }

    @Override
    public void correlationPermissions(Long roleId, Long... permissionIds) {

    }

    @Override
    public void uncorrelationPermissions(Long roleId, Long... permissionIds) {

    }

    @Override
    public void deleteAll() {
        roleDao.deleteAll();
    }
}

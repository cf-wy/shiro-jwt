package com.shiro.demo.dao;

import com.shiro.demo.model.Permission;
import tk.mybatis.mapper.common.Mapper;

public interface PermissionDao extends Mapper<Permission> {
    void deleteAll();
}

package com.shiro.demo.dao;

import com.shiro.demo.model.Role;
import tk.mybatis.mapper.common.Mapper;

public interface RoleDao extends Mapper<Role> {
    void deleteAll();
}

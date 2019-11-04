package com.shiro.demo.dao;

import com.shiro.demo.model.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.Set;

public interface UserDao extends Mapper<User> {
    User findByUsername(String userName);

    void deleteAll();

    Set<String> findRoles(String username);

    Set<String> findPermissions(String username);
}

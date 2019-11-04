package com.shiro.demo.service.impl;

import com.shiro.demo.dao.RoleDao;
import com.shiro.demo.dao.UserDao;
import com.shiro.demo.model.User;
import com.shiro.demo.service.UserService;
import com.shiro.demo.util.PasswordHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Override
    public int createUser(User user) {
        //加密密码
        PasswordHelper.encryptPassword(user);
        return userDao.insertSelective(user);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user =userDao.selectByPrimaryKey(userId);
        user.setPassword(newPassword);
        PasswordHelper.encryptPassword(user);
        userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public void correlationRoles(Long userId, Long... roleIds) {

    }

    @Override
    public void uncorrelationRoles(Long userId, Long... roleIds) {

    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional
    public Set<String> findRoles(String username) {
        return userDao.findRoles(username);
    }

    @Override
    @Transactional
    public Set<String> findPermissions(String username) {
        return userDao.findPermissions(username);
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }
}

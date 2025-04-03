package com.guestroom.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guestroom.entity.User;
import com.guestroom.mapper.UserMapper;
import com.guestroom.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            user.setUpdateTime(LocalDateTime.now());
            updateById(user);
            return user;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean register(User user) {
        if (getUserByUsername(user.getUsername()) != null) {
            return false;
        }
        if (user.getPhone() != null && lambdaQuery().eq(User::getPhone, user.getPhone()).count() > 0) {
            return false;
        }
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return save(user);
    }

    @Override
    public User getUserById(Integer id) {
        return getById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        user.setUpdateTime(LocalDateTime.now());
        return updateById(user);
    }

    @Override
    @Transactional
    public boolean updateUserStatus(Integer id, String status) {
        User user = getById(id);
        if (user == null) {
            return false;
        }
        user.setStatus(Integer.parseInt(status));
        user.setUpdateTime(LocalDateTime.now());
        return updateById(user);
    }

    @Override
    @Transactional
    public boolean updatePassword(Integer id, String oldPassword, String newPassword) {
        User user = getById(id);
        if (user == null || !oldPassword.equals(user.getPassword())) {
            return false;
        }
        user.setPassword(newPassword);
        user.setUpdateTime(LocalDateTime.now());
        return updateById(user);
    }

    @Override
    public List<User> getUserList(Integer pageNum, Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        return page(page).getRecords();
    }

    @Override
    @Transactional
    public boolean deleteUser(Integer id) {
        return removeById(id);
    }
}

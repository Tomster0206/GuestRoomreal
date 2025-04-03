package com.guestroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guestroom.entity.User;
import java.util.List;

public interface UserService extends IService<User> {
    // 用户登录
    User login(String username, String password);
    
    // 用户注册
    boolean register(User user);
    
    // 根据ID查询用户
    User getUserById(Integer id);
    
    // 根据用户名查询用户
    User getUserByUsername(String username);
    
    // 更新用户信息
    boolean updateUser(User user);
    
    // 更新用户状态
    boolean updateUserStatus(Integer id, String status);
    
    // 更新用户密码
    boolean updatePassword(Integer id, String oldPassword, String newPassword);
    
    // 分页查询用户列表
    List<User> getUserList(Integer pageNum, Integer pageSize);
    
    // 删除用户
    boolean deleteUser(Integer id);
}

package com.guestroom.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guestroom.common.Result;
import com.guestroom.entity.Customer;
import com.guestroom.entity.User;
import com.guestroom.entity.dto.LoginRequest;
import com.guestroom.service.CustomerService;
import com.guestroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            return Result.success(user);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public boolean register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PutMapping
    public boolean updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/status")
    public boolean updateUserStatus(@PathVariable Integer id, @RequestParam String status) {
        return userService.updateUserStatus(id, status);
    }

    @PutMapping("/{id}/password")
    public boolean updatePassword(@PathVariable Integer id,
                                  @RequestParam String oldPassword,
                                  @RequestParam String newPassword) {
        return userService.updatePassword(id, oldPassword, newPassword);
    }

    @GetMapping
    public Page<User> getUserList(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        return userService.page(page);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }
}

package com.guestroom.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guestroom.common.Result;
import com.guestroom.entity.Customer;
import com.guestroom.entity.dto.LoginRequest;
import com.guestroom.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/all")
    public Result getAll() {
        return Result.success(customerService.getAll());
    }

    @PostMapping("/login")
    public Result customerLogin(@RequestBody LoginRequest loginRequest) {
        Customer customer = customerService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (customer != null) {
            return Result.success(customer);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/add")
    public Result addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer) ? Result.success() : Result.error("添加失败");
    }

    @PostMapping("/update")
    public Result updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer) ? Result.success() : Result.error("更新失败");
    }

    @GetMapping("/{id}")
    public Result getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return customer != null ? Result.success(customer) : Result.error("客户不存在");
    }

    @GetMapping("/phone/{phone}")
    public Result getCustomerByPhone(@PathVariable String phone) {
        Customer customer = customerService.getCustomerByPhone(phone);
        return customer != null ? Result.success(customer) : Result.error("客户不存在");
    }

    @GetMapping("/idcard/{idCard}")
    public Result getCustomerByIdCard(@PathVariable String idCard) {
        Customer customer = customerService.getCustomerByIdCard(idCard);
        return customer != null ? Result.success(customer) : Result.error("客户不存在");
    }

    @GetMapping("/page")
    public Result getCustomerList(@RequestParam(defaultValue = "1") Integer pageNum,
                                  @RequestParam(defaultValue = "10") Integer pageSize, Customer customer) {
        Page<Customer> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(customer.getName()), Customer::getName, customer.getName());
        return Result.success(customerService.page(page, wrapper));
    }

    @PutMapping("/{id}/credit")
    public Result updateCustomerCreditScore(@PathVariable Long id, @RequestParam Integer creditScore) {
        return customerService.updateCustomerCreditScore(id, creditScore) ?
                Result.success() : Result.error("信用评分更新失败");
    }

    @GetMapping("/{id}/bookings")
    public Result getCustomerBookings(@PathVariable Long id) {
        return Result.success(customerService.getCustomerBookings(id));
    }

    @PostMapping("/delete/{id}")
    public Result deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id) ? Result.success() : Result.error("删除失败");
    }
}

package com.guestroom.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guestroom.Vo.PageVo;
import com.guestroom.entity.Booking;
import com.guestroom.entity.Customer;
import com.guestroom.mapper.CustomerMapper;
import com.guestroom.service.BookingService;
import com.guestroom.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Autowired
    private BookingService bookingService;

    @Override
    @Transactional
    public boolean addCustomer(Customer customer) {
        // 检查手机号是否已存在
        if (lambdaQuery().eq(Customer::getPhone, customer.getPhone()).one() != null) {
            return false;
        }
        // 检查身份证号是否已存在
        if (lambdaQuery().eq(Customer::getIdCard, customer.getIdCard()).one() != null) {
            return false;
        }
        if (StrUtil.isEmpty(customer.getPassword())) {
            customer.setPassword("123456");
        }
        customer.setCreateTime(LocalDateTime.now());
        customer.setUpdateTime(LocalDateTime.now());
        return save(customer);
    }

    @Override
    @Transactional
    public boolean updateCustomer(Customer customer) {
        customer.setUpdateTime(LocalDateTime.now());
        return updateById(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return getById(id);
    }

    @Override
    public Customer getCustomerByPhone(String phone) {
        return lambdaQuery().eq(Customer::getPhone, phone).one();
    }

    @Override
    public Customer getCustomerByIdCard(String idCard) {
        return lambdaQuery().eq(Customer::getIdCard, idCard).one();
    }

    @Override
    public List<Customer> searchCustomers(String name, String phone, String customerType) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            queryWrapper.like(Customer::getName, name);
        }
        if (phone != null) {
            queryWrapper.eq(Customer::getPhone, phone);
        }
        if (customerType != null) {
            queryWrapper.eq(Customer::getCustomerType, customerType);
        }
        return list(queryWrapper);
    }

    @Override
    @Transactional
    public boolean updateCustomerCreditScore(Long id, Integer creditScore) {
        Customer customer = getById(id);
        if (customer == null) {
            return false;
        }
        customer.setCreditScore(creditScore);
        customer.setUpdateTime(LocalDateTime.now());
        return updateById(customer);
    }

    @Override
    public List<Booking> getCustomerBookings(Long customerId) {
        return bookingService.getByCustomerId(customerId);
    }

    @Override
    public Customer login(String username, String password) {
        Customer customer = getCustomerByPhone(username);
        if (customer != null && password.equals(customer.getPassword())) {
            customer.setUpdateTime(LocalDateTime.now());
            updateById(customer);
            return customer;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteCustomer(Long id) {
        return removeById(id);
    }

    @Override
    public List<Customer> getAll() {
        return list();
    }

    @Override
    public Long total(String name) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            queryWrapper.like(Customer::getName, name);
        }
        return count(queryWrapper);
    }

    @Override
    public List<Customer> list(PageVo pageVo) {
        Page<Customer> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        return page(page).getRecords();
    }
}

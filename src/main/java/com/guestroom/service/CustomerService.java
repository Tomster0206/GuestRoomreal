package com.guestroom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guestroom.Vo.PageVo;
import com.guestroom.entity.Customer;
import com.guestroom.entity.Booking;
import java.util.List;

public interface CustomerService extends IService<Customer> {
    
    // 分页查询
    List<Customer> list(PageVo pageVo);
    Long total(String keyword);
    
    // 客户基本信息操作
    boolean addCustomer(Customer customer);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(Long id);
    
    // 客户查询
    Customer getCustomerById(Long id);
    Customer getCustomerByPhone(String phone);
    Customer getCustomerByIdCard(String idCard);
    List<Customer> getAll();
    
    // 客户搜索
    List<Customer> searchCustomers(String name, String phone, String customerType);

    boolean updateCustomerCreditScore(Long id, Integer creditScore);
    
    // 客户预订
    List<Booking> getCustomerBookings(Long customerId);

    Customer login(String username, String password);
}

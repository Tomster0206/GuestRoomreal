package com.guestroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guestroom.entity.Customer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
} 
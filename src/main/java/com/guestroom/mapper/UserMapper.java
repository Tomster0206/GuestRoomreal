package com.guestroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guestroom.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
} 
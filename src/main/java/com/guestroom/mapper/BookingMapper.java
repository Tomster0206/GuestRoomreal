package com.guestroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guestroom.entity.Booking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDate;

@Mapper
public interface BookingMapper extends BaseMapper<Booking> {
    
    @Select("SELECT COUNT(*) FROM booking WHERE DATE(create_time) = #{date}")
    Integer countBookingsByDate(LocalDate date);
    
    @Select("SELECT COUNT(*) FROM booking WHERE DATE(check_in_time) = #{date} AND status = 'CHECKED_IN'")
    Integer countCheckinsByDate(LocalDate date);
} 
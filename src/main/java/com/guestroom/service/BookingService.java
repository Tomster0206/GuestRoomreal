package com.guestroom.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guestroom.entity.Booking;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

public interface BookingService extends IService<Booking> {
    
    /**
     * 创建预订
     */
    boolean createBooking(Booking booking);
    
    /**
     * 根据客户ID查询预订列表
     */
    List<Booking> getByCustomerId(Long customerId);
    
    /**
     * 根据房间ID查询预订列表
     */
    List<Booking> getByRoomId(Long roomId);
    
    /**
     * 根据状态查询预订列表
     */
    List<Booking> getBookingsByStatus(Integer status);
    
    /**
     * 分页查询预订列表
     */
    Page<Booking> page(Page<Booking> page);

    /**
     * 删除预订
     */
    boolean deleteBooking(Long id);
    
    /**
     * 计算总价
     */
    BigDecimal calculateTotalPrice(Long roomId, LocalDateTime checkInTime, LocalDateTime checkOutTime);

    boolean updateBookingStatus(Long id, String status);
}
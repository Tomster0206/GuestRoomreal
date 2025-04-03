package com.guestroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guestroom.entity.Booking;
import com.guestroom.entity.Room;
import com.guestroom.mapper.BookingMapper;
import com.guestroom.service.BookingService;
import com.guestroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingServiceImpl extends ServiceImpl<BookingMapper, Booking> implements BookingService {

    @Autowired
    private RoomService roomService;

    @Override
    @Transactional
    public boolean createBooking(Booking booking) {
        // 检查房间是否可用
        LambdaQueryWrapper<Room> roomQueryWrapper = new LambdaQueryWrapper<>();
        roomQueryWrapper.eq(Room::getRoomNumber, booking.getRoomId());
        Room room = roomService.getOne(roomQueryWrapper);
        if (room == null || room.getStatus() != 0) {
            return false;
        }

        // 设置初始状态
        booking.setStatus("PENDING");
        booking.setCreateTime(LocalDateTime.now());
        booking.setUpdateTime(LocalDateTime.now());

        // 计算总价
        booking.setTotalPrice(calculateTotalPrice(booking.getRoomId(), booking.getCheckInTime(), booking.getCheckOutTime()).add(booking.getDeposit()));
        boolean flag = save(booking);

        // 修改房间状态
        room.setStatus(1);
        roomService.updateById(room);
        return flag;
    }

    @Override
    @Transactional
    public boolean updateBookingStatus(Long id, String status) {
        Booking booking = getById(id);
        booking.setStatus(status);
        booking.setUpdateTime(LocalDateTime.now());
        LambdaQueryWrapper<Room> roomQueryWrapper = new LambdaQueryWrapper<>();
        roomQueryWrapper.eq(Room::getRoomNumber, booking.getRoomId());
        Room room = roomService.getOne(roomQueryWrapper);
        if ("CONFIRMED".equals(status)) {
            room.setStatus(2);
            booking.setPaymentStatus("PAID");
            updateById(booking);
        } else if ("CANCELLED".equals(status)) {
            room.setStatus(0);
            deleteBooking(id);
        } else if ("COMPLETED".equals(status)) {
            room.setStatus(0);
            updateById(booking);
        }
        return roomService.updateById(room);
    }

    @Override
    public List<Booking> getByCustomerId(Long customerId) {
        return lambdaQuery().eq(Booking::getCustomerId, customerId).list();
    }

    @Override
    public List<Booking> getByRoomId(Long roomId) {
        return lambdaQuery().eq(Booking::getRoomId, roomId).list();
    }

    @Override
    public List<Booking> getBookingsByStatus(Integer status) {
        return lambdaQuery().eq(Booking::getStatus, status).list();
    }

    @Override
    public Page<Booking> page(Page<Booking> page) {
        return super.page(page);
    }

    @Override
    @Transactional
    public boolean deleteBooking(Long id) {
        return removeById(id);
    }

    @Override
    public BigDecimal calculateTotalPrice(Long roomId, LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        LambdaQueryWrapper<Room> roomQueryWrapper = new LambdaQueryWrapper<>();
        roomQueryWrapper.eq(Room::getRoomNumber, roomId);
        Room room = roomService.getOne(roomQueryWrapper);
        if (room == null) {
            return BigDecimal.ZERO;
        }

        long days = ChronoUnit.DAYS.between(checkInTime, checkOutTime);
        if (days <= 0) {
            days = 1;
        }

        return BigDecimal.valueOf(room.getPrice()).multiply(BigDecimal.valueOf(days));
    }
} 
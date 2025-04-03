package com.guestroom.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guestroom.common.Result;
import com.guestroom.entity.Booking;
import com.guestroom.entity.Customer;
import com.guestroom.entity.Room;
import com.guestroom.service.BookingService;
import com.guestroom.service.CustomerService;
import com.guestroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private RoomService roomService;

    @PostMapping("/add")
    public Result<Void> addBooking(@RequestBody Booking booking) {
        // 设置默认状态
        if (booking.getStatus() == null) {
            booking.setStatus("PENDING");
        }
        if (booking.getPaymentStatus() == null) {
            booking.setPaymentStatus("UNPAID");
        }

        // 验证必填字段
        if (booking.getCustomerId() == null || booking.getRoomId() == null
                || booking.getCheckInTime() == null || booking.getCheckOutTime() == null) {
            return Result.error("必填字段不能为空");
        }

        // 验证时间
        if (booking.getCheckOutTime().isBefore(booking.getCheckInTime())) {
            return Result.error("退房时间不能早于入住时间");
        }

        // 验证押金
        if (booking.getDeposit() == null || booking.getDeposit().compareTo(BigDecimal.ZERO) < 0) {
            return Result.error("押金不能为负数");
        }

        // 验证支付方式
        if (booking.getPaymentMethod() == null || booking.getPaymentMethod().trim().isEmpty()) {
            booking.setPaymentMethod("CASH");
        }

        return bookingService.createBooking(booking) ? Result.success() : Result.error("创建预订失败");
    }

    @PostMapping("updateStatus/{id}/{status}")
    public Result<Void> updateBookingStatus(@PathVariable Long id, @PathVariable String status) {
        return bookingService.updateBookingStatus(id, status) ? Result.success() : Result.error("修改订单状态失败");
    }

    @GetMapping("/{id}")
    public Result getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getById(id);
        return booking != null ? Result.success(booking) : Result.error("预订不存在");
    }

    @GetMapping("/customer/{customerId}")
    public Result getBookingsByCustomerId(@PathVariable Long customerId) {
        return Result.success(bookingService.getByCustomerId(customerId));
    }

    @GetMapping("/room/{roomId}")
    public Result getBookingsByRoomId(@PathVariable Long roomId) {
        return Result.success(bookingService.getByRoomId(roomId));
    }

    @GetMapping("/status/{status}")
    public Result getBookingsByStatus(@PathVariable Integer status) {
        return Result.success(bookingService.getBookingsByStatus(status));
    }

    @GetMapping("/page")
    public Result getBookingList(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(required = false) String name) {
        Page<Booking> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Customer> customerQueryWrapper = new QueryWrapper<>();
        QueryWrapper<Booking> queryWrapper = new QueryWrapper<>();
        // 如果传入了客户名称，添加模糊查询条件
        if (StrUtil.isNotBlank(name)) {
            customerQueryWrapper.like("name", name);
        }
        List<Customer> customers = customerService.list(customerQueryWrapper);
        List<Long> customerIds = customers.stream().map(Customer::getId).collect(Collectors.toList());
        queryWrapper.in("customer_id", customerIds);

        // 执行分页查询
        Page<Booking> result = bookingService.page(page, queryWrapper);
        result.getRecords().forEach(booking -> {
            QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
            Customer customer = customers.stream().filter(c -> c.getId().equals(booking.getCustomerId())).findFirst().get();
            booking.setCustomerName(customer.getName());
            roomQueryWrapper.eq("room_number", booking.getRoomId());
            Room room = roomService.getOne(roomQueryWrapper);
            booking.setRoomType(room.getRoomType());
        });
        return Result.success(result);
    }

    @GetMapping("/list")
    public Result getBookingList() {
        QueryWrapper<Booking> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("check_in_time");
        List<Booking> list = bookingService.list(queryWrapper).stream().limit(10).collect(Collectors.toList());
        list.forEach(booking -> {
            Customer customer = customerService.getById(booking.getCustomerId());
            booking.setCustomerName(customer.getName());
        });
        return Result.success(list);
    }

    @GetMapping("/calculate-price")
    public Result calculateTotalPrice(@RequestParam Long roomId,
                                      @RequestParam LocalDateTime checkInTime,
                                      @RequestParam LocalDateTime checkOutTime) {
        BigDecimal totalPrice = bookingService.calculateTotalPrice(roomId, checkInTime, checkOutTime);
        return Result.success(totalPrice);
    }

    @PostMapping("/delete/{id}")
    public Result<Void> deleteBooking(@PathVariable Long id) {
        return bookingService.deleteBooking(id) ? Result.success() : Result.error("删除失败");
    }
}
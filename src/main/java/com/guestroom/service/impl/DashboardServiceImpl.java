package com.guestroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guestroom.dto.DashboardStatsDTO;
import com.guestroom.dto.BookingTrendDTO;
import com.guestroom.entity.Booking;
import com.guestroom.entity.Room;
import com.guestroom.mapper.BookingMapper;
import com.guestroom.mapper.RoomMapper;
import com.guestroom.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private BookingMapper bookingMapper;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // 获取今日数据
        stats.setTotalRooms(roomMapper.countTotalRooms());
        stats.setAvailableRooms(roomMapper.countAvailableRooms());
        stats.setTodayBookings(bookingMapper.countBookingsByDate(today));
        stats.setTodayCheckins(bookingMapper.countCheckinsByDate(today));

        // 获取昨日数据
        stats.setYesterdayTotalRooms(roomMapper.countTotalRoomsByDate(yesterday));
        stats.setYesterdayAvailable(roomMapper.countAvailableRoomsByDate(yesterday));
        stats.setYesterdayBookings(bookingMapper.countBookingsByDate(yesterday));
        stats.setYesterdayCheckins(bookingMapper.countCheckinsByDate(yesterday));

        return stats;
    }

    @Override
    public BookingTrendDTO getBookingTrend(int days) {
        BookingTrendDTO trend = new BookingTrendDTO();
        List<String> dates = new ArrayList<>();
        List<Integer> bookingCounts = new ArrayList<>();
        List<Double> revenues = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        // 获取最近几天的数据
        for (int i = days - 1; i >= 0; i--) {
            LocalDateTime date = now.minusDays(i);
            LocalDateTime dayStart = date.with(LocalTime.MIN);
            LocalDateTime dayEnd = date.with(LocalTime.MAX);

            // 添加日期
            dates.add(date.format(formatter));

            // 查询当天的预订数量
            QueryWrapper<Booking> countQuery = new QueryWrapper<>();
            countQuery.between("create_time", dayStart, dayEnd);
            Long count = bookingMapper.selectCount(countQuery);
            bookingCounts.add(count.intValue());

            // 查询当天的收入
            QueryWrapper<Booking> revenueQuery = new QueryWrapper<>();
            revenueQuery.between("create_time", dayStart, dayEnd)
                    .eq("payment_status", "PAID");
            BigDecimal revenue = bookingMapper.selectList(revenueQuery)
                    .stream()
                    .map(Booking::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            revenues.add(revenue.doubleValue());
        }

        trend.setDates(dates);
        trend.setBookingCounts(bookingCounts);
        trend.setRevenues(revenues);

        return trend;
    }
} 
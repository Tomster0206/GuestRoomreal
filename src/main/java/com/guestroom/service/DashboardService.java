package com.guestroom.service;

import com.guestroom.dto.DashboardStatsDTO;
import com.guestroom.dto.BookingTrendDTO;

public interface DashboardService {
    /**
     * 获取首页统计数据
     * @return 统计数据DTO
     */
    DashboardStatsDTO getDashboardStats();

    /**
     * 获取预订趋势数据
     * @param days 获取最近几天的数据
     * @return 趋势数据DTO
     */
    BookingTrendDTO getBookingTrend(int days);
} 
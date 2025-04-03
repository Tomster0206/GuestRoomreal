package com.guestroom.controller;

import com.guestroom.common.Result;
import com.guestroom.dto.DashboardStatsDTO;
import com.guestroom.dto.BookingTrendDTO;
import com.guestroom.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取首页统计数据
     * @return 统计数据
     */
    @GetMapping("/stats")
    public Result<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        return Result.success(stats);
    }

    /**
     * 获取预订趋势数据
     * @param days 获取最近几天的数据，默认7天
     * @return 趋势数据
     */
    @GetMapping("/trend")
    public Result<BookingTrendDTO> getBookingTrend(@RequestParam(defaultValue = "7") int days) {
        BookingTrendDTO trend = dashboardService.getBookingTrend(days);
        return Result.success(trend);
    }
} 
package com.guestroom.dto;

import lombok.Data;

@Data
public class DashboardStatsDTO {
    // 总房间数
    private Integer totalRooms;
    
    // 可用房间数
    private Integer availableRooms;
    
    // 今日预订数
    private Integer todayBookings;
    
    // 今日入住数
    private Integer todayCheckins;
    
    // 昨日总房间数
    private Integer yesterdayTotalRooms;
    
    // 昨日可用房间数
    private Integer yesterdayAvailable;
    
    // 昨日预订数
    private Integer yesterdayBookings;
    
    // 昨日入住数
    private Integer yesterdayCheckins;
    
    // 本月预订数
    private Integer monthlyBookings;
    
    // 本月收入
    private Double monthlyRevenue;
    
    // 房间使用率(百分比)
    private Double occupancyRate;
} 
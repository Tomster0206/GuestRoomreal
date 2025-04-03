package com.guestroom.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookingTrendDTO {
    // 日期列表
    private List<String> dates;
    
    // 预订数量列表
    private List<Integer> bookingCounts;
    
    // 收入金额列表
    private List<Double> revenues;
} 
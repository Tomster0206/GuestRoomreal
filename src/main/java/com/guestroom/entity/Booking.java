package com.guestroom.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("booking")
public class Booking extends BaseEntity {

    private Long customerId;
    private Long roomId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkOutTime;
    private String status;
    private BigDecimal totalPrice;
    private BigDecimal deposit;
    private String paymentStatus;
    private String paymentMethod;
    private String remark;
    @TableField(exist = false)
    private String customerName;
    @TableField(exist = false)
    private String roomType;
}
package com.guestroom.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户信息实体类
 * @TableName customer
 */
@Data
@ToString
public class Customer implements Serializable {
    private Long id;
    private String name;
    private String sex;
    private Integer age;
    private String phone;
    private String idCard;
    private String email;
    private String address;
    private String customerType;
    private Integer creditScore;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;
    private String password;
}
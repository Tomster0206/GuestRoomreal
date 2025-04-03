package com.guestroom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客房信息实体类
 * @TableName room
 */
@Data
@TableName("room")
public class Room implements Serializable {

    private String roomNumber;
    private String roomType;
    private Integer floor;
    private Double price;
    private Integer status;
    private String description;
    private Integer maxGuests;
    private String facilities;
    private Double area;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableLogic
    private Integer deleted;
}
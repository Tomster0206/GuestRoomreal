-- 用户表
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `real_name` VARCHAR(50) COMMENT '真实姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `status` INT NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    `role` VARCHAR(20) NOT NULL COMMENT '角色：ADMIN-管理员，USER-普通用户',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` INT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 房间表
CREATE TABLE `room` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '房间ID',
    `room_number` VARCHAR(20) NOT NULL COMMENT '房间号',
    `room_type` VARCHAR(50) NOT NULL COMMENT '房间类型：STANDARD-标准间，DELUXE-豪华间，SUITE-套房',
    `price` DECIMAL(10,2) NOT NULL COMMENT '房间价格',
    `status` INT NOT NULL DEFAULT 0 COMMENT '状态：0-空闲，1-已预订，2-已入住，3-维护中',
    `facilities` TEXT COMMENT '房间设施',
    `floor` INT NOT NULL COMMENT '所在楼层',
    `area` DECIMAL(10,2) NOT NULL COMMENT '房间面积',
    `description` TEXT COMMENT '房间描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` INT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_room_number` (`room_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间表';

-- 客户表
CREATE TABLE `customer` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '客户ID',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `sex` VARCHAR(10) NOT NULL COMMENT '性别',
    `age` INT COMMENT '年龄',
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
    `id_card` VARCHAR(18) NOT NULL COMMENT '身份证号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `address` VARCHAR(200) COMMENT '地址',
    `customer_type` VARCHAR(20) NOT NULL DEFAULT 'REGULAR' COMMENT '客户类型：REGULAR-普通，VIP-会员',
    `credit_score` INT DEFAULT 100 COMMENT '信用评分',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` INT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_id_card` (`id_card`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 预订表
CREATE TABLE `booking` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '预订ID',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `room_id` BIGINT NOT NULL COMMENT '房间ID',
    `check_in_time` DATETIME NOT NULL COMMENT '入住时间',
    `check_out_time` DATETIME NOT NULL COMMENT '退房时间',
    `total_price` DECIMAL(10,2) NOT NULL COMMENT '总价',
    `deposit` DECIMAL(10,2) NOT NULL COMMENT '押金',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING-待确认，CONFIRMED-已确认，CANCELLED-已取消，COMPLETED-已完成',
    `payment_status` VARCHAR(20) NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态：UNPAID-未支付，PAID-已支付，REFUNDED-已退款',
    `payment_method` VARCHAR(20) COMMENT '支付方式',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` INT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_room_id` (`room_id`),
    KEY `idx_check_in_time` (`check_in_time`),
    KEY `idx_check_out_time` (`check_out_time`),
    CONSTRAINT `fk_booking_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
    CONSTRAINT `fk_booking_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预订表';

-- 插入默认管理员账号
INSERT INTO `user` (`username`, `password`, `role`, `status`) 
VALUES ('admin', 'admin123', 'ADMIN', 1); 
package com.guestroom.service;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guestroom.entity.Room;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface RoomService extends IService<Room> {
    // 获取可用房间列表
    List<Room> getAvailableRooms(String checkInTime, String checkOutTime);

    /**
     * 推荐房间列表
     * @param checkInTime 入住时间
     * @param checkOutTime 退房时间
     * @param sortType 排序类型
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    IPage<Room> recommend(String checkInTime, String checkOutTime, String sortType, Integer pageNum, Integer pageSize);
}

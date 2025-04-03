package com.guestroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guestroom.entity.Booking;
import com.guestroom.entity.Room;
import com.guestroom.mapper.BookingMapper;
import com.guestroom.mapper.RoomMapper;
import com.guestroom.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    private final BookingMapper bookingMapper;

    public RoomServiceImpl(BookingMapper bookingMapper) {
        this.bookingMapper = bookingMapper;
    }

    @Override
    public List<Room> getAvailableRooms(String checkInTime, String checkOutTime) {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getStatus, 0)  // 0 表示空闲状态
                .notExists("SELECT 1 FROM booking b WHERE b.room_id = room.id " +
                        "AND b.check_in_time <= {1}" +
                        "AND b.check_out_time >= {0}", checkInTime, checkOutTime)
                .orderByDesc(Room::getCreateTime);
        return list(wrapper);
    }

    @Override
    public IPage<Room> recommend(String checkInTime, String checkOutTime, String sortType, Integer pageNum, Integer pageSize) {
        // 1. 创建分页对象
        Page<Room> page = new Page<>(pageNum, pageSize);

        // 2. 构建查询条件
        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();

        // 3. 获取已预订的房间ID列表
        List<Long> bookedRoomIds = getBookedRoomIds(checkInTime, checkOutTime);
        if (!bookedRoomIds.isEmpty()) {
            queryWrapper.notIn("room_number", bookedRoomIds);
        }

        // 4. 根据不同的排序类型设置排序规则
        switch (sortType) {
            case "PRICE_ASC":
                queryWrapper.orderByAsc("price");
                break;
            case "PRICE_DESC":
                queryWrapper.orderByDesc("price");
                break;
            case "AREA_DESC":
                queryWrapper.orderByDesc("area");
                break;
            case "RECOMMEND":
            default:
                // 推荐算法：综合考虑价格、面积、楼层等因素
                // 这里使用一个简单的评分公式：score = price * 0.4 + area * 0.3 + floor * 0.3
                queryWrapper.orderByDesc("(price * 0.4 + area * 0.3 + floor * 0.3)");
                break;
        }

        // 5. 执行分页查询
        return page(page, queryWrapper);
    }

    /**
     * 获取指定时间段内已预订的房间ID列表
     */
    private List<Long> getBookedRoomIds(String checkInTime, String checkOutTime) {
        QueryWrapper<Booking> bookingQuery = new QueryWrapper<>();
        bookingQuery
                .in("status", "PENDING", "CONFIRMED", "COMPLETED")
                .and(wrapper -> wrapper
                        .lt("check_in_time", checkOutTime)
                        .gt("check_out_time", checkInTime)
                )
                .select("DISTINCT room_id");
        List<Booking> bookings = bookingMapper.selectList(bookingQuery);
        return bookings.stream()
                .map(Booking::getRoomId)
                .distinct()
                .collect(Collectors.toList());
    }

}

package com.guestroom.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guestroom.common.Result;
import com.guestroom.entity.Room;
import com.guestroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping("/add")
    public Result addRoom(@RequestBody Room room) {
        return roomService.save(room) ? Result.success() : Result.error("添加失败");
    }

    @PostMapping("/update")
    public Result updateRoom(@RequestBody Room room) {
        return roomService.updateById(room) ? Result.success() : Result.error("更新失败");
    }

    @PostMapping("/{id}/status")
    public Result updateRoomStatus(@PathVariable Long id, @RequestParam Integer status) {
        Room room = new Room();
        room.setId(id);
        room.setStatus(status);
        return roomService.updateById(room) ? Result.success() : Result.error("状态更新失败");
    }

    @GetMapping("/{id}")
    public Result getRoomById(@PathVariable Long id) {
        Room room = roomService.getById(id);
        return room != null ? Result.success(room) : Result.error("房间不存在");
    }

    @GetMapping("/number/{roomNumber}")
    public Result getRoomByNumber(@PathVariable String roomNumber) {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getRoomNumber, roomNumber);
        Room room = roomService.getOne(wrapper);
        return room != null ? Result.success(room) : Result.error("房间不存在");
    }

    @GetMapping("/type/{roomType}")
    public Result getRoomsByType(@PathVariable String roomType) {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getRoomType, roomType);
        return Result.success(roomService.list(wrapper));
    }

    @GetMapping("/status/{status}")
    public Result getRoomsByStatus(@PathVariable Integer status) {
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Room::getStatus, status);
        return Result.success(roomService.list(wrapper));
    }

    @GetMapping
    public Result getRoomList(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize, Room room) {
        Page<Room> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Room> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotEmpty(room.getRoomNumber()), Room::getRoomNumber, room.getRoomNumber())
                .eq(StrUtil.isNotEmpty(room.getRoomType()), Room::getRoomType, room.getRoomType())
                .eq(ObjectUtil.isNotEmpty(room.getStatus()), Room::getStatus, room.getStatus());
        return Result.success(roomService.page(page, wrapper));
    }

    @GetMapping("/available")
    public Result getAvailableRooms(@RequestParam String checkInTime,
                                    @RequestParam String checkOutTime) {
        return Result.success(roomService.getAvailableRooms(checkInTime, checkOutTime));
    }

    @PostMapping("/delete/{id}")
    public Result deleteRoom(@PathVariable Long id) {
        return roomService.removeById(id) ? Result.success() : Result.error("删除失败");
    }

    @GetMapping("/recommend")
    public Result recommend(
            @RequestParam String checkInTime,
            @RequestParam String checkOutTime,
            @RequestParam(defaultValue = "RECOMMEND") String sortType,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(roomService.recommend(checkInTime, checkOutTime, sortType, pageNum, pageSize));
    }
}

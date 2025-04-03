package com.guestroom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guestroom.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDate;

@Mapper
public interface RoomMapper extends BaseMapper<Room> {
    
    @Select("SELECT COUNT(*) FROM room")
    Integer countTotalRooms();
    
    @Select("SELECT COUNT(*) FROM room WHERE status = 'AVAILABLE'")
    Integer countAvailableRooms();
    
    @Select("SELECT COUNT(*) FROM room WHERE DATE(create_time) = #{date}")
    Integer countTotalRoomsByDate(LocalDate date);
    
    @Select("SELECT COUNT(*) FROM room WHERE status = 'AVAILABLE' AND DATE(create_time) = #{date}")
    Integer countAvailableRoomsByDate(LocalDate date);
}
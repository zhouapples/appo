package com.meeting.appo.dao;

import com.meeting.appo.entities.Room;
import com.meeting.appo.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventRoomDao {

    void addRoom(@Param("room") Room room);

    void modRoom(Room room);

    void rmRoom(Integer rid);

    int getRoomCount();

    List<Room> queryAllRooms();
    List<Room> queryAllRoomsByadmin();


}

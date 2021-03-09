package com.meeting.appo.dao;

import com.meeting.appo.entities.Room;
import com.meeting.appo.entities.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EventRoomDao {

    void addRoom(@Param("room") Room room);

    void modRoom(Integer rid);

    void rmRoom(Integer rid);

    List<Room> queryAllRooms();
}

package com.meeting.appo.dao;

import com.meeting.appo.entities.Room;
import com.meeting.appo.entities.Status;
import com.meeting.appo.entities.User;

import java.util.List;

public interface StatusMapper {
    // 获取所有数据列表
    List<Status> getStatusList();
    //根据日期查询
    Status getStatusByDay(String day);
    //根据用户id查询
    Status getStatusByUser(Integer uid);
    //根据房间id查询
    Status getStatusByRoom(Integer rid);

    //增加房间
    void addRoom(Room room);
    //增加用户
    void addUser(User user);
    //增加状态
    void addStatue(Status status);

    //修改用户
    void updateUser(Object obj);
    //修改房间
    void updateRoom(Room room);
    //修改状态
    void updateStatue(Status status);

    //删除用户
    void deleteUser(Integer uid);
    //删除房间
    void deleteRoom(Integer rid);
    //删除状态
    void deleteStatue(Integer sid);

}

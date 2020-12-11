package com.meeting.appo.services;

import com.meeting.appo.entities.Room;
import com.meeting.appo.entities.Status;
import com.meeting.appo.entities.User;

import java.util.List;

public interface Services {
    // 获取所有数据列表
    List<Status> getStatusList();
    //根据日期查询
    Status getStatusByDay(Integer day);
    //根据用户id查询
    Status getStatusByUser(User uid);
    //根据房间id查询
    Status getStatusByRoom(Room rid);

    //增加房间
    void addRoom(Room room);
    //增加用户
    void addUser(User user);
    //增加状态
    void addStatus(Status status);

    //修改用户
    void updateUser(Integer uid);
    //修改房间
    void updateRoom(Integer rid);
    //修改状态
    void updateStatus(Integer sid);

    //删除用户
    void deleteUser(Integer uid);
    //删除房间
    void deleteRoom(Integer rid);
    //删除状态
    void deleteStatus(Integer sid);

}

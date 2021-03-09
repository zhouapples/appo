package com.meeting.appo.dao;

import com.meeting.appo.entities.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EventUserDao {

    void addUser(@Param("user") User user);

    void rmUser(Integer uid);

    void modUser(Map<String,Object> infoMap);

    List<User> queryAllUsers();

}
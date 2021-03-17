package com.meeting.appo.dao;

import com.meeting.appo.entities.Dept;
import com.meeting.appo.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

@Mapper
public interface EventUserDao {

    void addUser(@Param("user") User user);

    void rmUser(Integer uid);

    void modUser(@Param("user") User user);

    List<User> queryAllUsers();

    void addDept(@Param("dept") Dept dept);

    User getUserById(int uid);

}

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
    void modUser(Map<String,Object> userMap);

    List<User> queryAllUsers();

    User getUserById(int uid);

    int getUserCount();

    void resetPwd(String pwd,int uid);





    //add
    void addDept(@Param("dept") Dept dept);

    //get-count
    int getDeptCount();

    //update
    void modDept(@Param("dept") Dept dept);

    //remove
    void rmDept(int did);


}

package com.meeting.appo.dao;

import com.meeting.appo.entities.Dept;
import com.meeting.appo.entities.Room;
import com.meeting.appo.entities.Status;
import com.meeting.appo.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;


@Mapper
public interface EventStatusDao {
    //other
    List<Status> getStatusList(String day, @Nullable String rid);
    List<Room> getRooms();
    User getUserByUsername(String username);
    String getAdminByUserId(Integer id);
    List<Status> getStatusByUser(int uid,@Nullable Long start_timestamp);

    //增单
    void addStatus(@Param("status") Status status);

    //改单
    void modStatus(@Param("status") Status status);

    //删单
    void rmStatusById(Integer sid);

    //查全
    List<Status> queryAllStatus();

    //通过id查
    Status getStatusBySid(Integer sid);

    //查全部部门
    List<Dept> getAllDept();



}

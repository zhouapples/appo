package com.meeting.appo.dao;

import com.meeting.appo.entities.Room;
import com.meeting.appo.entities.Status;
import com.meeting.appo.entities.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

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
    //删除房间 @Param()只能用在方法的参数上面,而且方法存在多个参数,所有参数都必须加上这个注解
    //@Param这个注解将会把参数传给sql语句,注意@Param中参数名要和数据库字段名一致
    @Insert("insert into appo.tb_rooms(rid,rflood,rserial,seats,isAvailable,comm,create_date) " +
            "values(#{rid},#{rflood},#{rserial},#{seats},#{isAvailable},#{comm},#{create_date})")
    void addRoom(@Param("rid") int id,
                 @Param("rflood") int flood,
                 @Param("rserial") String serial,
                 @Param("seats") int seats,
                 @Param("isAvailable") boolean available,
                 @Param("comm") String comment,
                 @Param("create_date") String dateStr
                 );
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

    //删除房间 @Param()只能用在方法的参数上面,而且方法存在多个参数,所有参数都必须加上这个注解
    //@Param这个注解将会把参数传给sql语句,注意@Param中参数名要和数据库字段名一致
    @Delete("delete from appo.tb_rooms where rid= #{rid} and rflood=#{rflood}")
    void deleteRoom(@Param("rid") int bianhao, @Param("rflood") int louceng);
    //删除状态
    void deleteStatue(Integer sid);

}

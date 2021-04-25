package com.meeting.appo;

import com.meeting.appo.dao.EventRoomDao;
import com.meeting.appo.dao.EventStatusDao;
import com.meeting.appo.dao.EventUserDao;
import com.meeting.appo.entities.Status;

import com.meeting.appo.entities.User;
import org.apache.ibatis.session.SqlSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.activation.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class AppoApplicationTests {

    @Autowired
    EventStatusDao statusDao;
    @Autowired
    EventUserDao userDao;
    @Autowired
    EventRoomDao roomDao;

    @Test
    void contextLoads() {
    }

    @Test
    public void getStatusList(){

        List<Status> statusList = statusDao.getStatusList("2021-03-10",null);
        for (Status s:statusList){
            System.out.println(s);
        }
    }

    @Test
    public void getAdminByUserId(){

        String admin = statusDao.getAdminByUserId(3);
        System.out.println(admin);

    }


    @Test
    public void getUserByUsername(){


    }

    @Test
    public void modUser(){
        User user = new User();
        user.setUsername("欧巴桑");
        user.setUid(4);
        user.setMobile("13188339541");
        userDao.modUser(user);

    }

    @Test
    public void getStatusByUser(){

        List<Status> statusList = statusDao.getStatusByUser(1,null);
        for (Status s:statusList){
            System.out.println(s);
        }

    }

    @Test
    public void getStatusBySid(){
        Status s = statusDao.getStatusBySid(1);
        System.out.println(s);

    }


    @Test
    public void getHintData(){
        Map<String,Object> objects = statusDao.showHistogram();
        System.out.println(objects);
    }

    @Test
    public void getWeek() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse("2021-03-24"));
        int w = cal.get(Calendar.DAY_OF_WEEK)-1<0?0:cal.get(Calendar.DAY_OF_WEEK)-1;
        System.out.println(w);

    }


}

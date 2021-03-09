package com.meeting.appo;

import com.meeting.appo.dao.EventStatusDao;
import com.meeting.appo.dao.EventUserDao;
import com.meeting.appo.entities.Status;
import com.meeting.appo.utils.WebUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class AppoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void getStatusList(){
        SqlSession sqlSession = WebUtils.getSqlSession();
        EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
        List<Status> statusList = mapper.getStatusList("2021-03-08",null);
        for (Status s:statusList){
            System.out.println(s);
        }
        sqlSession.close();
    }

    @Test
    public void getAdminByUserId(){
        SqlSession sqlSession = WebUtils.getSqlSession();
        EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
        String admin = mapper.getAdminByUserId(3);
        System.out.println(admin);
        sqlSession.close();
    }


    @Test
    public void getUserByUsername(){
        SqlSession sqlSession = WebUtils.getSqlSession();
        EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
        Map map = mapper.getUserByUsername("欧巴");
        System.out.println(map);
        sqlSession.close();
    }

    @Test
    public void modUser(){
        SqlSession sqlSession = WebUtils.getSqlSession();
        EventUserDao userDao = sqlSession.getMapper(EventUserDao.class);
        Map<String,Object> infoMap = new HashMap<String, Object>();
        infoMap.put("username","欧巴桑");
        infoMap.put("uid",4);
        infoMap.put("dept","惊呆部");
        infoMap.put("mobile","13188339541");
        userDao.modUser(infoMap);
        sqlSession.close();
    }

    @Test
    public void getStatusByUser(){
        SqlSession sqlSession = WebUtils.getSqlSession();
        EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
        List<Status> statusList = mapper.getStatusByUser(1,null);
        for (Status s:statusList){
            System.out.println(s);
        }
        sqlSession.close();
    }




}

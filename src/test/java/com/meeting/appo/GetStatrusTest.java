package com.meeting.appo;

import com.meeting.appo.dao.StatusMapper;
import com.meeting.appo.entities.Status;
import com.meeting.appo.entities.User;
import com.meeting.appo.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetStatrusTest {

    @Test
    public void showAll(){
        //1.获取sqlsession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        //2.获取mapper
        StatusMapper mapper = sqlSession.getMapper(StatusMapper.class);

        List<Status> statusList = mapper.getStatusList();
        for (Status s:statusList) {
            System.out.println(s);
        }
        //3.关闭SQLSession
        sqlSession.close();

    }

    @Test
    public void addOneUser(){
        //1.获取sqlSession对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StatusMapper mapper = sqlSession.getMapper(StatusMapper.class);
        mapper.addUser(new User(3,"蟹老板","13388639451","物业","2012/10/22 12:25:59",false));
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void getStatusByUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StatusMapper mapper = sqlSession.getMapper(StatusMapper.class);
        Status status = mapper.getStatusByUser(1);
        System.out.println(status);
    }

    @Test
    public void updateUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StatusMapper mapper = sqlSession.getMapper(StatusMapper.class);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("myName","章鱼哥");
        map.put("uid",1);
        mapper.updateUser(map);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void deleteUser(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StatusMapper mapper = sqlSession.getMapper(StatusMapper.class);
        mapper.deleteUser(3);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void deleteRoom(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StatusMapper mapper = sqlSession.getMapper(StatusMapper.class);
        mapper.deleteRoom(1,401);
        sqlSession.close();
    }


    @Test
    public void addRoom(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        StatusMapper mapper = sqlSession.getMapper(StatusMapper.class);
        mapper.addRoom(4,1,"101",10,true,"","2020/10/01");
        sqlSession.commit();
        sqlSession.close();
    }



}

package com.meeting.appo;

import com.meeting.appo.dao.EventStatusDao;
import com.meeting.appo.entities.Status;
import com.meeting.appo.utils.WebUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.EventListener;
import java.util.List;

@SpringBootTest
class AppoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void getStatusList(){
        SqlSession sqlSession = WebUtils.getSqlSession();
        EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
        List<Status> statusList = mapper.getStatusList();
        for (Status s:statusList){
            System.out.println(s);
        }
        sqlSession.close();
    }




}

package com.meeting.appo.controller;

import com.meeting.appo.dao.EventStatusDao;
import com.meeting.appo.entities.Status;
import com.meeting.appo.utils.WebUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DetailController {

    @GetMapping("/detail")
    public String toDetailPage(@RequestParam("date") String day, Model model){
        System.out.println("----------已选择日期 "+day+"---------------");

        SqlSession sqlSession = WebUtils.getSqlSession();
        EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
        List<Status> statusList = mapper.getStatusList(day);
        sqlSession.close();

        model.addAttribute(statusList);

        model.addAttribute("msg","已选择日期 "+day);
        return "status_page";
    }

}

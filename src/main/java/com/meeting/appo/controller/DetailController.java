package com.meeting.appo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.appo.dao.EventStatusDao;
import com.meeting.appo.entities.Room;
import com.meeting.appo.entities.Status;
import com.meeting.appo.utils.WebUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DetailController {

    @GetMapping("/detail")
    public String toDetailPage(HttpServletRequest request, Model model)  {


        String day = request.getParameter("date");
        String rid = request.getParameter("rid");

        SqlSession sqlSession = WebUtils.getSqlSession();
        EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
        List<Status> statusList = mapper.getStatusList(day,rid);
        List<Room> roomsList = mapper.getRooms();
        sqlSession.close();


        model.addAttribute("statusList",statusList);
        model.addAttribute("rooms",roomsList);
        System.out.println(roomsList);
        model.addAttribute("msg",day);
        return "status_page";
    }

    @ResponseBody
    @GetMapping("/detail/item_detail")
    public String getItemDetail(HttpServletRequest request) throws JsonProcessingException {
        int sid = Integer.parseInt(request.getParameter("sid"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        SqlSession sqlSession = WebUtils.getSqlSession();
        EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
        Status status = mapper.getStatusBySid(sid);
        sqlSession.close();
        Map<String,Object> tempMap = new HashMap<String, Object>();
        tempMap.put("sid",status.getSid());
        tempMap.put("create_date",sdf.format(status.getCreate_date()));
        tempMap.put("start_date",sdf.format(status.getStart_date()));
        tempMap.put("end_date",sdf.format(status.getEnd_date()));
        tempMap.put("participants",status.getParticipants());
        tempMap.put("meeting_theme",status.getMeeting_theme());
        tempMap.put("username",status.getUser().getUsername());
        tempMap.put("mobile",status.getUser().getMobile());
        tempMap.put("dept",status.getUser().getDept());
        tempMap.put("rserial",status.getRoom().getRserial());
        return new ObjectMapper().writeValueAsString(tempMap);
    }


    @DeleteMapping("/profile")
    public void deleteStatusById(HttpServletRequest request, HttpServletResponse response){
        String sidStr = request.getParameter("sid");
        if (sidStr!=null){
            int sid = Integer.parseInt(sidStr);
            SqlSession sqlSession = WebUtils.getSqlSession();
            EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
            try {
                mapper.rmStatusById(sid);
                sqlSession.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                sqlSession.close();
            }

            System.out.println("取得sidStr值为:"+sidStr);

        }

    }



}

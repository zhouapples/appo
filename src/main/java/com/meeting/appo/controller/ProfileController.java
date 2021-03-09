package com.meeting.appo.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.appo.dao.EventRoomDao;
import com.meeting.appo.dao.EventStatusDao;
import com.meeting.appo.dao.EventUserDao;
import com.meeting.appo.entities.Room;
import com.meeting.appo.entities.Status;
import com.meeting.appo.entities.User;
import com.meeting.appo.utils.WebUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String toUserProfile(HttpSession session,Model model){
        Map user = (Map)session.getAttribute("loginUser");
        int uid = (int) user.get("uid");
        SqlSession sqlSession = WebUtils.getSqlSession();

        //构造查询起始时间,当前时间前推一个月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-1);    //前推1个月
        Date m = calendar.getTime();
        Long start_timestamp = (long)(int)(m.getTime()/1000);
        System.out.println(start_timestamp);

        EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
        List<Status> statusList = mapper.getStatusByUser(uid,start_timestamp);
        sqlSession.close();
        model.addAttribute("statusList",statusList);
        return "profile";
    }

    @PostMapping("/profile/update_user")
    public String updateUser(HttpServletRequest request,
                             HttpSession session,
                             SessionStatus sessionStatus,
                             Model model){
        String username = request.getParameter("username");
        String mobile = request.getParameter("mobile");
        String dept = request.getParameter("dept");
        int uid = Integer.parseInt(request.getParameter("uid"));

        //输入数据校验
        if(username.equals("") || mobile.equals("") || dept.equals("")){
            model.addAttribute("errMsg","信息不完整,用户名和部门用文字或字母,号码用数字");
            return "redirect:/profile";
        }else if(mobile.length() != 11){
            model.addAttribute("errMsg","手机号码长度有误");
            return "redirect:/profile";
        }else{
            //校验通过
            SqlSession sqlSession = WebUtils.getSqlSession();
            EventUserDao mapper = sqlSession.getMapper(EventUserDao.class);
            Map<String,Object> infoMap = new HashMap<String, Object>();
            infoMap.put("uid",uid);
            infoMap.put("username",username);
            infoMap.put("mobile",mobile);
            infoMap.put("dept",dept);
            mapper.modUser(infoMap);
            sqlSession.commit();
            sqlSession.close();


            //清除session信息并跳转到登陆页面
            session.invalidate();
            sessionStatus.setComplete();
            request.setAttribute("msg","资料已更新,请重新登陆!");
            return "redirect:/login";
        }
    }

    @GetMapping("/rs_manage")
    public String toManagePage(HttpServletRequest request,Model model){
        HttpSession session = request.getSession();
        Map loginUser = (Map)session.getAttribute("loginUser");
        boolean isAdmin = (boolean)loginUser.get("isAdmin");
        if (isAdmin){
            //渲染数据



            return "rs_manage";
        }
            return "404";
    }

    @PostMapping("/profile/updateRoom")
    public String updateRoom(){

        return "rs_manage";
    }

    @PostMapping("/profile/updateStatus")
    public String updateStatus(){

        return "rs_manage";
    }


    @ResponseBody
    @PostMapping("/profile/addStatus")
    public String addStatus(HttpServletRequest request) throws JsonProcessingException {
        SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate = null, startDate = null;
        Map<String,Object> statusMap = new HashMap<String,Object>();
        SqlSession sqlSession = WebUtils.getSqlSession();

        try {
            startDate = sdfm.parse(request.getParameter("start_date"));
            endDate = sdfm.parse(request.getParameter("end_date"));
            if (endDate.getTime() < startDate.getTime()){
                throw new ParseException("开始时间小于结束时间",1);//确保开始时间大于结束时间
            }

            int rid = Integer.parseInt(request.getParameter("rid"));
            int uid = Integer.parseInt(request.getParameter("uid"));
            String participants = request.getParameter("participants");
            String meetingTheme = request.getParameter("meeting_theme");
            boolean status = !request.getParameter("status").equals("0");


            Status state = new Status(new Date(),startDate,endDate,rid,participants,meetingTheme,uid,status);

            EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
            mapper.addStatus(state);
            List<Status> todayStatusList = mapper.getStatusList(new SimpleDateFormat("yyyy-MM-dd").format(startDate),rid+"");

            //判断预约冲突
            for (Status s:todayStatusList){
                if ((s.getStart_date().getTime()<startDate.getTime() && startDate.getTime()<s.getEnd_date().getTime()) ||
                        (s.getStart_date().getTime()>startDate.getTime() && endDate.getTime()>s.getStart_date().getTime()) ||
                        (s.getStart_date().getTime()<startDate.getTime() && startDate.getTime()>s.getEnd_date().getTime())
                ){
                    throw new ParseException(s.toString(),1);
                }
            }
            sqlSession.commit();
            sqlSession.close();
            statusMap.put("code",200);
            statusMap.put("msg","命令成功完成");
        } catch (ParseException e) {
            e.printStackTrace();
            sqlSession.rollback();
            sqlSession.close();
            statusMap.put("code",500);
            statusMap.put("errMsg","操作失败");
        }
        return new ObjectMapper().writeValueAsString(statusMap);
    }

    @ResponseBody
    @PostMapping("/profile/addUser")
    public String addUser(HttpServletRequest request,Model model,HttpServletResponse response) throws ServletException, IOException {
        boolean admin;
        String username = request.getParameter("username");
        String mobile = request.getParameter("mobile");
        String dept = request.getParameter("dept");
        String isAdmin = request.getParameter("isAdmin");
        Map<String,String> deptMap = new HashMap<String, String>();
        deptMap.put("0","震惊部");
        deptMap.put("1","沸腾部");
        deptMap.put("2","掉泪部");
        deptMap.put("3","沉默部");

        admin = !isAdmin.equals("0") && !isAdmin.equals("false");

        //注册校验
        if(username.equals("") || mobile.equals("") || dept.equals("")){
            model.addAttribute("errMs","信息不完整,用户名和部门用文字或字母,号码用数字");
            request.getRequestDispatcher("/rs_manage").forward(request,response);
        }else if(mobile.length() <11){
            model.addAttribute("errMs","手机号码长度有误");
            request.getRequestDispatcher("/rs_manage").forward(request,response);
        }
        //校验通过
        User user = new User(username,mobile,deptMap.get(dept),new Date(),admin);
        SqlSession sqlSession = WebUtils.getSqlSession();
        EventUserDao mapper = sqlSession.getMapper(EventUserDao.class);
        mapper.addUser(user);
        sqlSession.commit();
        sqlSession.close();
        request.getRequestDispatcher("/rs_manage").forward(request,response);
        Map<String,Object> respMap = new HashMap<String,Object>();
        respMap.put("code",200);
        respMap.put("success",true);
        return new ObjectMapper().writeValueAsString(respMap);

    }


    @PostMapping("/profile/addRoom")
    public void addRoom(HttpServletRequest request){
        int flood = Integer.parseInt(request.getParameter("rflood"));
        String serial = (String)request.getParameter("rserial");
        int seats = Integer.parseInt(request.getParameter("seats"));
        boolean available = !request.getParameter("isAvailable").equals("");
        String comm = (String)request.getParameter("comm");

        SqlSession sqlSession = WebUtils.getSqlSession();
        EventRoomDao mapper = sqlSession.getMapper(EventRoomDao.class);
        Room room = new Room(flood,serial,seats,available,comm,new Date());
        mapper.addRoom(room);
        System.out.println(room.toString());
        sqlSession.commit();
        sqlSession.close();
    }






    @GetMapping("/test")
    public String chooseTemp(){
        return "rs_manage";
    }




}

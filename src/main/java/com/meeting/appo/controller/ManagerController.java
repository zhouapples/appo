package com.meeting.appo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.meeting.appo.entities.Dept;
import com.meeting.appo.entities.Room;
import com.meeting.appo.entities.Status;
import com.meeting.appo.entities.User;


import com.meeting.appo.services.RoomService;
import com.meeting.appo.services.StatusService;
import com.meeting.appo.services.UserService;
import com.meeting.appo.utils.AdminAuthUtils;
import com.meeting.appo.utils.Chinese2PinyinUtils;
import com.meeting.appo.utils.SecEncodeUtils;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ManagerController {

    @Autowired
    UserService userService;
    @Autowired
    StatusService statusService;
    @Autowired
    RoomService roomService;



    @GetMapping("/admin")
    public String toAdminPage(HttpServletRequest request,Model model) throws JsonProcessingException, ParseException {
        if (!AdminAuthUtils.adminAuth(request)){
            return "404";
        }
        int userCount = userService.getUserCount();
        int statusCount = statusService.getStatusCount(null);
        int roomCount = roomService.getRoomCount();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        int today_appo =  statusService.getStatusCount(today);

        model.addAttribute("total_user",userCount);
        model.addAttribute("total_room",roomCount);
        model.addAttribute("total_status",statusCount);
        model.addAttribute("today_appo",today_appo);
        return "admin";
    }

    @ResponseBody
    @GetMapping("/admin/getDataset")
    public String getDataset() throws ParseException, JsonProcessingException {
        Map<String,Object> hintDataset = statusService.showHistogram();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<Integer,Long> dataSet = new HashMap<Integer, Long>();
        Calendar cal = Calendar.getInstance();

        for (Map.Entry e:hintDataset.entrySet()){
            cal.setTime(sdf.parse((String) e.getKey()));
            int d = cal.get(Calendar.DAY_OF_WEEK)-1<0?0:cal.get(Calendar.DAY_OF_WEEK)-1;
            Map temp = (HashMap)e.getValue();
            Long value = (Long) temp.get("count");
            dataSet.put(d,value);
        }
        return new ObjectMapper().writeValueAsString(dataSet);
    }


    @GetMapping("/status_manager")
    public String room_manager(HttpServletRequest request, Model model){
        if (!AdminAuthUtils.adminAuth(request)){
            return "404";
        }
        List<Status> statusList = statusService.adminStatusList(0,  10);
        int totalStatusCount = statusService.getStatusCount(null);
        int pageNum = (int)Math.ceil(totalStatusCount / 10);

        model.addAttribute("statusList",statusList);    //当前加载页
        model.addAttribute("pageNum",pageNum);      //页码
        return "status_manager";
    }


    @PostMapping("/status_manager")
    public String showStatusByPageNum(HttpServletRequest request,Model model){
        if (!AdminAuthUtils.adminAuth(request)){
            return "404";
        }
        int start = 0;
        try {
            start = Integer.parseInt(request.getParameter("pageNum"))*10;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        List<Status> statusList = statusService.adminStatusList(start, 10); //limit 10,10第一个参数表示从下标为10的记录开始算,第二个参数表示显示多少条
        model.addAttribute("statusList",statusList);
        return "status_manager";
    }

    @ResponseBody
    @PutMapping("/setStatus")
    public String setStatusUnavailable(HttpServletRequest request, @Param("sid") int sid,
                                       @Param("state") int state,
                                       HttpSession session) throws JsonProcessingException {
        if (!AdminAuthUtils.adminAuth(request)){
            return "{errCode:403}";
        }

        Map<String,Object> userInfoObj = (Map<String,Object>) session.getAttribute("loginUser");
        int uid = (int)userInfoObj.get("uid");

        statusService.setStatusState(sid,state==1,uid);
        HashMap<String, Object> data = new HashMap<>();
        data.put("msg","success");
        if(state==1){
            data.put("res","已启用");
        }else{
            data.put("res","已禁用");
        }
        return new ObjectMapper().writeValueAsString(data);
    }


    @GetMapping("/rooms_manager")
    public String getRoomList(HttpServletRequest request,Model model){
        if (!AdminAuthUtils.adminAuth(request)){
            return "404";
        }
        List<Room> roomList = roomService.queryAllRoomsByadmin();
        model.addAttribute("roomList",roomList);
        return "rooms_manager";
    }

    @PutMapping("/setRoomState")
    public String setRoomState(HttpServletRequest request){
        if (!AdminAuthUtils.adminAuth(request)){
            return "404";
        }
        String comm = request.getParameter("comm");
        boolean available = request.getParameter("isAvailable").equals("1");
        int rid = Integer.parseInt(request.getParameter("rid"));
        int rflood = Integer.parseInt(request.getParameter("rflood"));
        int seats = Integer.parseInt(request.getParameter("seats"));
        String serial = request.getParameter("rserial");

        Room room = new Room();
        room.setRid(rid);
        room.setAvailable(available);
        room.setComm(comm);
        room.setRflood(rflood);
        room.setSeats(seats);
        room.setRserial(serial);
        roomService.modRoom(room);
        return "/rooms_manager";
    }

    @PostMapping("/setRoomState")
    public String addRoom( @Param("state") boolean available,
                           @Param("rflood") int rflood,
                           @Param("rserial") String rserial,
                           @Param("seats") int seats,
                           @Param("comm") String comm ,HttpServletRequest request){

        if (!AdminAuthUtils.adminAuth(request)){
            return "{errCode:403}";
        }

        Room room = new Room(rflood,rserial,seats,available,comm,new Date());
        roomService.addRoom(room);
        return "/rooms_manager";
    }

    @DeleteMapping("/setRoomState")
    public String removeRoom( @Param("rid") int rid,HttpServletRequest request){
        if (!AdminAuthUtils.adminAuth(request)){
            return "404";
        }
        roomService.rmRoom(rid);
        return "/rooms_manager";
    }



    @GetMapping("/users_manager")
    public String getUserManagerView(HttpServletRequest request,Model model){
        if (!AdminAuthUtils.adminAuth(request)){
            return "404";
        }
        List<User> userList = userService.queryAllUsers();
        List<Dept> deptList = statusService.getAllDept();
        model.addAttribute("userList",userList);
        model.addAttribute("deptList",deptList);
        return "users_manager";
    }

    @PutMapping("/setUserState")
    public void setUserState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AdminAuthUtils.adminAuth(request)){
            request.getRequestDispatcher("/login").forward(request,response);
        }
        int uid = Integer.parseInt(request.getParameter("uid"));
        String username = request.getParameter("username");
        String mobile = request.getParameter("mobile");
        int deptId = Integer.parseInt(request.getParameter("deptId"));
        boolean isAdmin = request.getParameter("isAdmin").equals("1");
        boolean available = request.getParameter("available").equals("1");

        String pinyin_name = Chinese2PinyinUtils.toPinyin(username);
        Map<String,Object> userMap = new HashMap<String,Object>();
        userMap.put("uid",uid);
        userMap.put("username",username);
        userMap.put("mobile",mobile);
        userMap.put("deptId",deptId);
        userMap.put("Admin",isAdmin);
        userMap.put("available",available);
        userMap.put("pinyin_name",pinyin_name);
        userService.modUser(userMap);
    }



    @DeleteMapping("/setUserState")
    public String deleteUser(HttpServletRequest request){
        if (!AdminAuthUtils.adminAuth(request)){
            return "404";
        }
        int uid = Integer.parseInt(request.getParameter("uid"));
        userService.rmUser(uid);
        return "users_manager";
    }

    @ResponseBody
    @PostMapping("/resetPwd")
    public String resetPwd(@Param("uid") int uid, HttpServletRequest request) throws JsonProcessingException {
        if (!AdminAuthUtils.adminAuth(request)){
            return "{errCode:403}";
        }
        String password = SecEncodeUtils.secEncode("123456");
        userService.resetPwd(password,uid);
        Map<String, Object> msg = new HashMap<String,Object>();
        msg.put("msg","重置成功");
        return new ObjectMapper().writeValueAsString(msg);
    }



    @GetMapping("/depts_manager")
    public String getDeptState(Model model){
        List<Dept> deptList = statusService.getAllDept();
        model.addAttribute("deptList",deptList);
        return "depts_manager";
    }

    @PutMapping("/setDeptState")
    public String setDeptState(HttpServletRequest request){
        if (!AdminAuthUtils.adminAuth(request)){
            return "404";
        }
        int did = Integer.parseInt(request.getParameter("did"));
        String dept_name = request.getParameter("dept_name");
        boolean is_first_level = request.getParameter("is_first_level").equals("1");
        int after_level_id = Integer.parseInt(request.getParameter("after_level_id"));
        String comment = request.getParameter("comment");
        int before_level_id = Integer.parseInt(request.getParameter("before_level_id"));

        Dept dept = new Dept(did,dept_name,before_level_id,after_level_id,comment,is_first_level);

        userService.modDept(dept);

        return "depts_manager";
    }



    @ResponseBody
    @DeleteMapping("/deleteDept")
    public String deleteDept(@Param("did") int did) throws JsonProcessingException {
        userService.rmDept(did);
        HashMap<String, String> msgMap = new HashMap<>();
        msgMap.put("msg","删除成功");
        return new ObjectMapper().writeValueAsString(msgMap);
    }




    @ResponseBody
    @RequestMapping("/admin/hint")
    public String showHint() throws JsonProcessingException {

        Map<String,Object> objects = statusService.showHistogram();
        return new ObjectMapper().writeValueAsString(objects);

    }






}

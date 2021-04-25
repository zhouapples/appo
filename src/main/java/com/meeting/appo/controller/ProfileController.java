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
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    UserService userService;
    @Autowired
    StatusService statusService;
    @Autowired
    RoomService roomService;


    @GetMapping("/profile")
    public String toUserProfile(HttpSession session,Model model){
        Map user = (Map)session.getAttribute("loginUser");
        int uid = (int) user.get("uid");


        //构造查询起始时间,当前时间前推一个月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH,-1);    //前推1个月,显示最近30天本人的预约记录
        Date m = calendar.getTime();
        Long start_timestamp = (long)(int)(m.getTime()/1000);
        System.out.println(start_timestamp);
        List<Status> statusList = statusService.getStatusByUser(uid,start_timestamp);
        model.addAttribute("statusList",statusList);
        return "profile";
    }

    @PostMapping("/profile/update_user")
    public String updateUser(HttpServletRequest request,
                             HttpSession session,
                             SessionStatus sessionStatus,
                             Model model){
        String username = request.getParameter("new_username");
        String old_password = request.getParameter("old_password");
        String new_password = request.getParameter("new_password");
        String mobile = request.getParameter("mobile");
        int deptId = Integer.parseInt(request.getParameter("deptId"));
        int uid = Integer.parseInt(request.getParameter("uid"));
        boolean isAdmin = request.getParameter("admin").equals("1");

        //输入数据校验
        if(username.equals("") || mobile.equals("") || old_password.equals("") ||new_password.equals("")){
            model.addAttribute("errMsg","信息不完整,用户名和部门用文字或字母,号码用数字");
            return "redirect:/profile";
        }else if(mobile.length() != 11) {
            model.addAttribute("errMsg", "手机号码长度有误");
            return "redirect:/profile";
        }
        else if (userService.getUserById(uid).getPassword().equals(old_password)){
            model.addAttribute("errMsg", "原密码有误");
            return "redirect:/profile";
        }else{
            //校验通过
            String pinyin_name = Chinese2PinyinUtils.toPinyin(username);
            User user = new User(uid,username,new_password,mobile,deptId,isAdmin,pinyin_name);
            userService.modUser(user);

            //清除session信息并跳转到登陆页面
            session.invalidate();
            sessionStatus.setComplete();
            request.setAttribute("msg","资料已更新,请重新登陆!");
            return "redirect:/login";
        }
    }

    @GetMapping("/rs_manage")
    public String toManagePage(HttpServletRequest request,Model model){
        if (AdminAuthUtils.adminAuth(request)){
            List<Dept> deptList = statusService.getAllDept();
            List<Room> roomList = roomService.queryAllRooms();

            model.addAttribute("deptList",deptList);
            model.addAttribute("roomList",roomList);

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
    public String addStatus(HttpServletRequest request,
                            HttpServletResponse response,
                            HttpSession session) throws IOException, ServletException {

        Object loginUser = session.getAttribute("loginUser");
        if (loginUser==null){
            //登陆检测，如未登录返回登陆界面
            request.setAttribute("msg","没有访问权限，请先登录");
            request.getRequestDispatcher("/login").forward(request,response);
        }

        SimpleDateFormat sdfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date endDate = null, startDate = null;
        Map<String,Object> statusMap = new HashMap<String,Object>();
        Status conflict = null;

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

            //查找在目标日期内,该目标房间的所有预约记录
            List<Status> todayStatusList = statusService.getStatusList(new SimpleDateFormat("yyyy-MM-dd").format(startDate),rid+"");

            //判断预约时间上的冲突
                for (Status s:todayStatusList){
                    if (!(startDate.getTime() < s.getStart_date().getTime() && endDate.getTime() < s.getStart_date().getTime()) &&
                            !(startDate.getTime() > s.getEnd_date().getTime())
                    ){
                        conflict = s;
                        throw new ParseException(s.toString(),1);
                    }
                }

            statusService.addStatus(state);
            statusMap.put("code",200);
            statusMap.put("msg","命令成功完成");
        } catch (ParseException e) {
            e.printStackTrace();
            statusMap.put("code",403);
            statusMap.put("user",conflict.getUser().getUsername());
            statusMap.put("mobile",conflict.getUser().getMobile());
        }
        return new ObjectMapper().writeValueAsString(statusMap);
    }

    @ResponseBody
    @PostMapping("/profile/addUser")
    public String addUser(HttpServletRequest request,Model model,HttpServletResponse response) throws ServletException, IOException {
        boolean admin;
        String username = request.getParameter("username");
        String mobile = request.getParameter("mobile");
        int deptId = Integer.parseInt(request.getParameter("deptId"));
        String isAdmin = request.getParameter("isAdmin");
        String password = request.getParameter("password");

        admin = !isAdmin.equals("0") && !isAdmin.equals("false");

        //注册校验
        if(username.equals("") || mobile.equals("") ){
            model.addAttribute("errMs","信息不完整,用户名和部门用文字或字母,号码用数字");
            request.getRequestDispatcher("/rs_manage").forward(request,response);
        }else if(mobile.length() <11){
            model.addAttribute("errMs","手机号码长度有误");
            request.getRequestDispatcher("/rs_manage").forward(request,response);
        }
        //校验通过
        String pinyin_name = Chinese2PinyinUtils.toPinyin(username);
        //密码加密器
        String sec_pwd = SecEncodeUtils.secEncode(password);
        User user = new User(username,sec_pwd,mobile,deptId,new Date(),admin,pinyin_name);
        userService.addUser(user);

//        request.getRequestDispatcher("/rs_manage").forward(request,response);
        Map<String,Object> respMap = new HashMap<String,Object>();
        respMap.put("code",200);
        respMap.put("success",true);
        return new ObjectMapper().writeValueAsString(respMap);
    }


    @PostMapping("/profile/addRoom")
    public void addRoom(HttpServletRequest request){
        int flood = Integer.parseInt(request.getParameter("rflood"));
        String serial = request.getParameter("rserial");
        int seats = Integer.parseInt(request.getParameter("seats"));
        boolean available = !request.getParameter("isAvailable").equals("");
        String comm = request.getParameter("comm");

        Room room = new Room(flood,serial,seats,available,comm,new Date());
        roomService.addRoom(room);
        System.out.println(room.toString());

    }


    @ResponseBody
    @PostMapping("/profile/addDept")
    public String addDept(HttpServletRequest request) throws JsonProcessingException {
        String dept_name = request.getParameter("dept_name");
        boolean is_first_level = !(request.getParameter("is_first_level").equals("0"))&&request.getParameter("is_first_level")!=null;

        int after_level_id;
        if (is_first_level){
            after_level_id = 0;
        }else {
            after_level_id = Integer.parseInt(request.getParameter("after_level_id"));
        }


        int before_level_id=0;
        String bli = request.getParameter("before_level_id");
        if (!bli.equals("")){
            before_level_id = Integer.parseInt(bli);
        }

        String comment = request.getParameter("comment");

        Dept dept = new Dept(dept_name,before_level_id,after_level_id,comment,is_first_level);

        userService.addDept(dept);

        HashMap<String, String> map = new HashMap<>();
        map.put("msg","ok");
        return new ObjectMapper().writeValueAsString(map);
    }


}

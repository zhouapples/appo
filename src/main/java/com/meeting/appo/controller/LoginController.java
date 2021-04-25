package com.meeting.appo.controller;


import com.meeting.appo.entities.Dept;
import com.meeting.appo.entities.User;

import com.meeting.appo.services.StatusService;
import com.meeting.appo.services.UserService;
import com.meeting.appo.utils.SecEncodeUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.support.SessionStatus;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    StatusService statusService;


    @GetMapping("/index")
    public String index(){
        return "index";
    }

    //来到登陆页
    @GetMapping("/login")
    public String getLogin(Model model){
        List<Dept> deptList = statusService.getAllDept();
        model.addAttribute("deptList",deptList);
        return "login";
    }

    //登录 post
    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpSession session, Model model){
        String pinyin_name = request.getParameter("pinyin_name");
        String pwd = request.getParameter("password");


        User user = statusService.getUserByUsername(pinyin_name);

        System.out.println(user);

        if (user !=null && SecEncodeUtils.secMacher(pwd,user.getPassword())){   //密码验证
            Object value = session.getAttribute("loginUser");
            if(value==null){

                Map<String,Object> map = new HashMap<String,Object>();
                map.put("uid",user.getUid());
                map.put("username",user.getUsername());
                map.put("mobile",user.getMobile());
                map.put("dept",user.getDept().getDept_name());
                map.put("isAdmin",user.isAdmin());
                session.setAttribute("loginUser",map);
            }
            model.addAttribute("username",user.getUsername());
            model.addAttribute("password",pwd);
            return "redirect:/index";
        }else{
            model.addAttribute("errMsg","用户名或者密码错误");
            return "login";
        }
    }


    //注册 post 接口
    @PostMapping("/regist")
    public String regist(HttpServletRequest request,HttpSession session,Model model){

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String mobile = request.getParameter("mobile");
        int deptId = Integer.parseInt(request.getParameter("deptId"));
        String isAdmin = request.getParameter("admin");

        boolean admin = isAdmin != null && isAdmin.equals("admin");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setMobile(mobile);
        user.setDeptId(deptId);
        user.setCreate_date(new Date());
        user.setAdmin(admin);
        //调service
        Map<String, Object> errMsgMap = userService.regUser(user);
        if(errMsgMap!=null){
            model.addAttribute("errMs",errMsgMap.get("errMs"));
            return "login";
        }

        return "redirect:/index";

    }

    // 登出
    @GetMapping("/logout")
    public String logout(HttpSession session, SessionStatus sessionStatus){
        session.invalidate();
        sessionStatus.setComplete();
        return "redirect:/login";
    }


}

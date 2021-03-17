package com.meeting.appo.controller;

import com.meeting.appo.dao.EventRoomDao;
import com.meeting.appo.dao.EventStatusDao;
import com.meeting.appo.dao.EventUserDao;
import com.meeting.appo.entities.User;

import com.meeting.appo.utils.Chinese2PinyinUtils;
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
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    EventStatusDao statusDao;
    @Autowired
    EventUserDao userDao;
    @Autowired
    EventRoomDao roomDao;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    //来到登陆页
    @GetMapping("/login")
    public String getLogin(Model model){
        Map<String,Object> msg = new HashMap<String,Object>();
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpSession session, Model model){
        String pinyin_name = request.getParameter("pinyin_name");
        String pwd = request.getParameter("password");

        User user = statusDao.getUserByUsername(pinyin_name);

        System.out.println(user);

        if (user !=null && pwd.equals(user.getMobile())){   //依然是把手机号码当做密码来认证
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



    @PostMapping("/regist")
    public String regist(HttpServletRequest request,HttpSession session,Model model){

        boolean admin=false;
        String username = request.getParameter("username");
        String mobile = request.getParameter("mobile");
        String dept = request.getParameter("dept");
        String isAdmin = request.getParameter("admin");

        if(isAdmin != null){
         if(isAdmin.equals("admin")){
             admin = true;
         }
        }
        //注册校验
        if(username.equals("") || mobile.equals("") || dept.equals("")){
            model.addAttribute("errMs","信息不完整,用户名和部门用文字或字母,号码用数字");
            return "login";
        }else if(mobile.length() <11){
            model.addAttribute("errMs","手机号码长度有误");
            return "login";
        }else{
            //校验通过
            String pinyin_name = Chinese2PinyinUtils.toPinyin(username);
            User user = new User(username,mobile,dept,new Date(),admin,pinyin_name);

            userDao.addUser(user);

            //注册完自动登陆
            User userReg = statusDao.getUserByUsername(username);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("uid",userReg.getUid());
            map.put("username",username);
            map.put("mobile",userReg.getMobile());
            map.put("dept",userReg.getDept().getDept_name());
            map.put("isAdmin",userReg.isAdmin());
            session.setAttribute("loginUser",map);
            return "redirect:/index";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session, SessionStatus sessionStatus){
        session.invalidate();
        sessionStatus.setComplete();
        return "redirect:/login";
    }


}

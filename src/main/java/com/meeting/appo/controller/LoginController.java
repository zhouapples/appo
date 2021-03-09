package com.meeting.appo.controller;

import com.meeting.appo.dao.EventStatusDao;
import com.meeting.appo.dao.EventUserDao;
import com.meeting.appo.entities.User;
import com.meeting.appo.utils.WebUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

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
        String username = request.getParameter("username");
        String pwd = request.getParameter("password");

        SqlSession sqlSession = WebUtils.getSqlSession();
        EventStatusDao mapper = sqlSession.getMapper(EventStatusDao.class);
        Map userMap = null;
        try {
            userMap = mapper.getUserByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        System.out.println(userMap);

        if (userMap !=null && pwd.equals(userMap.get("mobile"))){
            Object value = session.getAttribute("loginUser");
            if(value==null){

                Map<String,Object> map = new HashMap<String,Object>();
                map.put("uid",userMap.get("uid"));
                map.put("username",username);
                map.put("mobile",userMap.get("mobile"));
                map.put("dept",userMap.get("dept"));
                map.put("isAdmin",userMap.get("isAdmin"));
                session.setAttribute("loginUser",map);
            }
            model.addAttribute("username",username);
            model.addAttribute("password",pwd);
            return "redirect:/index";
        }else{
            model.addAttribute("errMsg","用户名或者密码错误");
            return "login";
        }


    }


    @GetMapping("/searchResult")
    public String searchResult(@RequestParam("name") String name,
                               @RequestParam("age") String age,
                               @RequestParam("gender") String gender,
                               Map<String,Object> map,HttpSession session){
        if (session.getAttribute("loginUser")==null){
            System.out.println("[name:"+name+" age:"+age+" gender:"+gender+"]");
            map.put("msg","请先登录");
            return "index";
        }else{
            map.put("msg","欢迎回来");
            System.out.println("正常查询");
            return "index";
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
            User user = new User(username,mobile,dept,new Date(),admin);
            SqlSession sqlSession = WebUtils.getSqlSession();
            EventUserDao mapper = sqlSession.getMapper(EventUserDao.class);
            mapper.addUser(user);
            sqlSession.commit();
            //注册完自动登陆
            EventStatusDao mapper1 = sqlSession.getMapper(EventStatusDao.class);
            Map userMap = mapper1.getUserByUsername(username);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("uid",userMap.get("uid"));
            map.put("username",username);
            map.put("mobile",userMap.get("mobile"));
            map.put("dept",userMap.get("dept"));
            map.put("isAdmin",userMap.get("isAdmin"));
            session.setAttribute("loginUser",map);
            sqlSession.close();
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

package com.meeting.appo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
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

    //接受登陆请求
    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String,Object> map , HttpSession session){

        if (!StringUtils.isEmpty(username) && password.equals("123456")){
            System.out.println("登陆成功");
            session.setAttribute("loginUser",username); //往session里面设置一个username属性
        }else{
            map.put("msg","用户名或者密码错误");
            return "login";
        }
        return "redirect:/index";
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

    @GetMapping("/detail/{date}")
    public String detail(@PathVariable("date") Integer day,Model model){
        System.out.println(day);

        return "";
    }



}

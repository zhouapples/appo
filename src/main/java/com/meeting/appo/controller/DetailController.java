package com.meeting.appo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DetailController {

    @GetMapping("/detail")
    public String toDetailPage(@RequestParam("date") Integer day, Model model){
        System.out.println("----------这是第"+day+"天---------------");
        return "list-view";
    }

}

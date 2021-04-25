package com.meeting.appo.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class AdminAuthUtils {
    public static boolean adminAuth(HttpServletRequest request){
        boolean temp=false;
        HttpSession session = request.getSession();
        Map loginUser = (Map)session.getAttribute("loginUser");
        try {
            temp = loginUser.get("isAdmin").equals(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}

package com.meeting.appo.utils;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecEncodeUtils {

    public static String secEncode(String rawPassword){
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    public static boolean secMacher(String rawPassword,String secPassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return  encoder.matches(rawPassword,secPassword);

    }


    public static void main(String[] args) {
        String s = secEncode("12345678");
        System.out.println(s);

        System.out.println("---------------------");
        boolean b = secMacher("12345678", s);
        System.out.println(b);


    }


}

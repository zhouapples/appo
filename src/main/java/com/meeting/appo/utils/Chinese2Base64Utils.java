package com.meeting.appo.utils;

import java.util.Base64;

public class Chinese2Base64Utils {
    public static String encodeBase64(String chinese){
        return new String(Base64.getEncoder().encode(chinese.getBytes()));
    }

    public static String decodeBase64(String base64Code){
        return new String(Base64.getDecoder().decode(base64Code.getBytes()));
    }
}

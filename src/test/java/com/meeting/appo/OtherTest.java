package com.meeting.appo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.appo.entities.User;
import com.meeting.appo.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jnlp.UnavailableServiceException;

public class OtherTest {

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void changeE() throws JsonProcessingException {
        String key = "user_Y2FuZ2ppbmdrb25n";
        String o = redisUtils.getString(key);
        System.out.println(o);
    }


}

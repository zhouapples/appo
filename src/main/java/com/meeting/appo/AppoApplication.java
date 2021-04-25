package com.meeting.appo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableCaching
@SpringBootApplication
public class AppoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppoApplication.class, args);
    }

}

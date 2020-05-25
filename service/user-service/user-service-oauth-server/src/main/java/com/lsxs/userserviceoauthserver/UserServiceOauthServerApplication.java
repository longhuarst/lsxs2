package com.lsxs.userserviceoauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@SpringBootApplication
@EnableEurekaClient
public class UserServiceOauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceOauthServerApplication.class, args);
    }

}

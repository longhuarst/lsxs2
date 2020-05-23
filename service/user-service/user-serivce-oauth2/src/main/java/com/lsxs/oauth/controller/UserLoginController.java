package com.lsxs.oauth.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserLoginController {

//    @PreAuthorize("true")
    @RequestMapping("/login")
    String login(){
        return "login";
    }


}

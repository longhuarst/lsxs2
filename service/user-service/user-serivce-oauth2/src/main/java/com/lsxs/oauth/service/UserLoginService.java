package com.lsxs.oauth.service;


import entity.AuthToken;

public interface UserLoginService {


    /*
    * 登陆操作
    * */
    AuthToken login(String username, String password, String clientId, String clientSecret, String grantType) throws Exception;
}

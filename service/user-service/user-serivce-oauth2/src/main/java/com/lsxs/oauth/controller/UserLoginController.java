package com.lsxs.oauth.controller;


import com.lsxs.oauth.repository.UserRepository;
import com.lsxs.oauth.tools.ValidCodeGenerator;
import com.lsxs.oauth.entity.EmailValid;
import com.lsxs.oauth.entity.User;
import com.lsxs.oauth.repository.EmailValidRepository;
import com.lsxs.oauth.service.EmailValidService;
import com.lsxs.oauth.service.UserLoginService;
import entity.AuthToken;
import entity.Result;
import entity.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/user")
public class UserLoginController {

    static Logger logger = LoggerFactory.getLogger(UserLoginController.class);

    //客户端id
    @Value("${auth.clientId}")
    private String clientId;

    //客户端密钥
    @Value("${auth.clientSecret}")
    private String clientSecret;


    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailValidService emailValidService;

    @Autowired
    private EmailValidRepository emailValidRepository;



//    @PreAuthorize("true")
    @RequestMapping("/login")
    public Result login(String username, String password) {


        //授权模式
        String grantType = "password";
        AuthToken authToken = null;

        try {
            authToken = userLoginService.login(username, password, clientId, clientSecret, grantType);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new Result(false, StatusCode.LOGINERROR, "登陆失败 : "+e.getMessage());
        }

        if (authToken != null){
            return new Result(true, StatusCode.OK, "登陆成功", authToken);
        }


        return new Result(false, StatusCode.LOGINERROR, "登陆失败");
    }



    @RequestMapping("/code")
    public Result code(String email){
        logger.info("email = " +email );

        if(!email.matches("^\\w+@(\\w+\\.)+\\w+$")) {
            return new Result(true, StatusCode.ERROR, "失败", "邮箱地址不正确");
        }

        User user = null;

        try {
            user = userRepository.findByMail(email);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }


        String code = ValidCodeGenerator.generate();

        EmailValid emailValid = new EmailValid();
        emailValid.setEmail(email);
        emailValid.setCode(code);


        //将邮件发送 加入 消息队列  异步发送

        emailValidService.add(email, code);




        try {

            EmailValid example = new EmailValid();
            example.setEmail(email);


            Optional<EmailValid> res = emailValidRepository.findOne(Example.of(example));


            if (res.isPresent()){
                logger.info("找到目标");
                emailValid.setId(res.get().getId());
                emailValid.setDate(new Date());
                logger.info("更新");

            }else{
                logger.info("未找到目标");
            }





            emailValidRepository.save(emailValid);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(true, StatusCode.OK, "成功", code);
    }



    //快速简易注册
    @RequestMapping("/registerSimple")
    public Result register(String username, String email, String password, String validCode) {
        return register(username, email, password, validCode, "", "", "", "","");
    }

    @RequestMapping("/register")
    public Result register(String username, String email, String password, String validCode ,String address, String nickName, String phone, String qq, String wx){
        User user = new User();

        if (user == null || email == null || password == null || validCode == null){
            return new Result(true, StatusCode.ERROR, "失败", "参数缺少");
        }

        if(!email.matches("^\\w+@(\\w+\\.)+\\w+$")) {
            return new Result(true, StatusCode.ERROR, "失败", "邮箱地址不正确");
        }

        //先校验用户是否存在
        User exampleUser = new User();
        exampleUser.setUsername(username);
        Optional<User> res= userRepository.findOne(Example.of(exampleUser));
        if (res.isPresent()){
            return new Result(true, StatusCode.ERROR, "注册失败","用户已存在");
        }


        //验证email
        EmailValid example = new EmailValid();
        example.setEmail(email);
        Optional<EmailValid> emailValid = emailValidRepository.findOne(Example.of(example));

        if (emailValid.isPresent()){
            Date cur = new Date();
            if (emailValid.get().getDate().getTime() - cur.getTime() > 1000*3600*24*2){
                return new Result(true, StatusCode.ERROR, "注册失败","验证码过期");
            }

            if (!emailValid.get().getCode().equals(validCode)){
                return new Result(true, StatusCode.ERROR, "注册失败","验证码错误");
            }
        }else{
            return new Result(true, StatusCode.ERROR, "注册失败","请先发送验证邮件");
        }


        logger.info("用户" + username+"邮箱"+email+"验证通过");


        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole("user");
        user.setUsername(username);
        user.setUuid(UUID.randomUUID().toString());
        user.setMail(email);
        user.setAddress(address);
        user.setName(nickName);
        user.setPhone(phone);
        user.setQq(qq);
        user.setWechat(wx);

        User resUser = null;
        try{
            resUser = userRepository.save(user);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "注册失败");
        }

        return new Result(true, StatusCode.OK, "注册成功", resUser);

    }


    @RequestMapping("userExist")
    public Result userExist(String username){
        //先校验用户是否存在
        User exampleUser = new User();
        exampleUser.setUsername(username);
        Optional<User> res= userRepository.findOne(Example.of(exampleUser));
        if (res.isPresent()){
            return new Result(true, StatusCode.ERROR, "失败","用户已存在");
        }

        return new Result(true, StatusCode.OK, "成功","用户不存在");

    }


    @RequestMapping("mailExist")
    public Result mailExist(String email){
        //先校验用户是否存在

        if(!email.matches("^\\w+@(\\w+\\.)+\\w+$")) {
            return new Result(true, StatusCode.ERROR, "失败", "邮箱地址不正确");
        }

        User exampleUser = new User();
        exampleUser.setMail(email);
        Optional<User> res= userRepository.findOne(Example.of(exampleUser));
        if (res.isPresent()){
            return new Result(true, StatusCode.ERROR, "失败","邮箱已存在");
        }

        return new Result(true, StatusCode.OK, "成功","邮箱不存在");

    }



}

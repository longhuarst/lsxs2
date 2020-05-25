package com.lsxs.oauth.configure;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(-1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {




    @Override
    public void configure(WebSecurity web) throws Exception {
        //super.configure(web);
        web.ignoring().antMatchers("/user/login",
                "/login.html",
                "/css/**",
                "/data/**",
                "/fonts/**",
                "/img/**",
                "/js/**");
    }
}

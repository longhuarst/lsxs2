package com.lsxs.oauth.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitmqConfig {


    //创建一个email 请求的 队列
    @Bean
    public Queue validEmailRequestQueue(){
        System.out.println("validEmailRequestQueue-------------------------");
        return new Queue("validEmailRequestQueue", true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("validEmailRequestExchange");
    }


    @Bean
    Binding bindingEmail(){
        return BindingBuilder.bind(validEmailRequestQueue()).to(topicExchange()).with("email.key");

    }


}

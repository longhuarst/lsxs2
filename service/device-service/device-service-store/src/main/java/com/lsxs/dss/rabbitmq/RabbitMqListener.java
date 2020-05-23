package com.lsxs.dss.rabbitmq;

import com.lsxs.dss.entity.RawMessage;
import com.lsxs.dss.entity.RawMessageSsl;
import com.lsxs.dss.repository.RawMessageRepository;
import com.lsxs.dss.repository.RawMessageSslRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqListener {

    @Autowired
    private RawMessageRepository rawMessageRepository;

    @Autowired
    private RawMessageSslRepository rawMessageSslRepository;

    Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);

    @RabbitHandler
    @RabbitListener(queues = "rabbitmq.DeviceServer.null.tx")
    public void listener(String msg){
//        System.out.printf(msg);//
        logger.info("收到 rabbitmq null 消息 ："+msg);
        RawMessage rawMessage = new RawMessage();
        rawMessage.setMsg(msg);
        rawMessageRepository.save(rawMessage);
    }


    @RabbitHandler
    @RabbitListener(queues = "rabbitmq.DeviceServer.ssl.tx")
    public void sslListener(String msg){
//        System.out.printf(msg);//
        logger.info("收到 rabbitmq ssl 消息 ："+msg);
        RawMessageSsl rawMessageSsl = new RawMessageSsl();
        rawMessageSsl.setMsg(msg);
        rawMessageSslRepository.save(rawMessageSsl);
    }
}

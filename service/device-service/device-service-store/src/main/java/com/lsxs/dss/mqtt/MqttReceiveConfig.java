package com.lsxs.dss.mqtt;

import com.lsxs.dss.entity.MqttMessage;
import com.lsxs.dss.repository.MqttMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
@IntegrationComponentScan
public class MqttReceiveConfig {

    @Autowired
    private MqttMessageRepository mqttMessageRepository;

    Logger logger = LoggerFactory.getLogger(MqttReceiveConfig.class);

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }


    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("tcp://www.hongyiweichuang.com:1883", "testClient",
                        "2dfa8c37-931d-4734-9253-5042f6459d9c:upload", "2dfa8c37-931d-4734-9253-5042f6459d9c:control", "test");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(message.getHeaders().get("mqtt_receivedTopic"));
                System.out.println(message.getPayload());

                logger.info("recv mqtt data ! ->  topic = ["+message.getHeaders().get("mqtt_receivedTopic")+"]  payload = ["+message.getPayload()+"]");


                //存储到服务器
                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setTopic(message.getHeaders().get("mqtt_receivedTopic").toString());
                mqttMessage.setMsg(message.getPayload().toString());
                mqttMessageRepository.save(mqttMessage);

            }

        };
    }


}

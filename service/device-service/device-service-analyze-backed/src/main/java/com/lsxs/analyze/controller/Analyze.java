package com.lsxs.analyze.controller;

import com.lsxs.analyze.entity.Location;
import com.lsxs.analyze.entity.MqttMessage;
import com.lsxs.analyze.repository.MqttMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analyze")
public class Analyze {

    @Autowired
    private MqttMessageRepository mqttMessageRepository;

    @RequestMapping("/last")
    MqttMessage last(){
        MqttMessage mqttMessage = mqttMessageRepository.getLastOne();
        return mqttMessage;
    }

    @RequestMapping("/lastTask")
    List<MqttMessage> lastTask(){
        return mqttMessageRepository.getLastTask();
    }

}

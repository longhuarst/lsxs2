package com.lsxs.analyze.repository;

import com.lsxs.analyze.entity.MqttMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MqttMessageRepository extends JpaRepository<MqttMessage, String> {

    @Query("select * from mqtt_message order by id DESC LIMIT 1")
    MqttMessage getLastOne();

    @Query("SELECT * from mqtt_message  where date > (select SUBDATE((select date from mqtt_message ORDER BY id DESC LIMIT 1),interval 30 minute))")
    List<MqttMessage> getLastTask();

}

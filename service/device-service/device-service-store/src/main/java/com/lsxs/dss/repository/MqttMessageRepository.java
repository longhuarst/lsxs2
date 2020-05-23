package com.lsxs.dss.repository;

import com.lsxs.dss.entity.MqttMessage;
import com.lsxs.dss.entity.RawMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MqttMessageRepository extends JpaRepository<MqttMessage, String> {
}

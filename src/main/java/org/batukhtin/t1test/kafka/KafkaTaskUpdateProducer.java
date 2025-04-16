package org.batukhtin.t1test.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.batukhtin.t1test.dto.TaskStatusUpdateMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(value = "t1.kafka.producer.enable",
    havingValue = "true",
    matchIfMissing = true)
public class KafkaTaskUpdateProducer {

    private final @Qualifier("kafkaMailProducer")KafkaTemplate<String, TaskStatusUpdateMessage> template;

    public void send(TaskStatusUpdateMessage message) {
        try {
            template.sendDefault(UUID.randomUUID().toString(), message);
            template.flush();
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendTo(String topic, TaskStatusUpdateMessage message){
        try {
            template.send(topic, message);
            template.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

package org.batukhtin.t1test.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.dto.TaskStatusUpdateMessage;
import org.batukhtin.t1test.mapper.TaskMapper;
import org.batukhtin.t1test.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTaskUpdateConsumer {

    private final NotificationService notificationService;
    private final TaskMapper taskMapper;

    @KafkaListener(id = "${t1.kafka.consumer.group-id}",
            topics = "${t1.kafka.topic.email_send}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<TaskStatusUpdateMessage> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("Client consumer: Обработка новых сообщений");
        try {
            messageList.forEach(task -> {
                notificationService.sendTaskStatusUpdate(task);
            });
        } finally {
            ack.acknowledge();
        }

        log.debug("Client записи обработаны");
    }
}

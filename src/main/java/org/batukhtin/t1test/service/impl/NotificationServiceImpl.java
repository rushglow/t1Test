package org.batukhtin.t1test.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.dto.TaskStatusUpdateMessage;
import org.batukhtin.t1test.model.TaskEntity;
import org.batukhtin.t1test.model.enums.TaskStatus;
import org.batukhtin.t1test.service.NotificationService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendTaskStatusUpdate(TaskStatusUpdateMessage taskStatusUpdateMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(taskStatusUpdateMessage.getEmail());
        message.setSubject("Task " + taskStatusUpdateMessage.getTaskId() +" Status Updated");
        message.setText("Task \"" + taskStatusUpdateMessage.getTitle() + "\" was updated to status: " + taskStatusUpdateMessage.getStatus());
        mailSender.send(message);
    }
}

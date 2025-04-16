package org.batukhtin.t1test.service;

import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.dto.TaskStatusUpdateMessage;
import org.batukhtin.t1test.model.TaskEntity;
import org.batukhtin.t1test.model.enums.TaskStatus;

public interface NotificationService {
    public void sendTaskStatusUpdate(TaskStatusUpdateMessage taskStatusUpdateMessage);
}

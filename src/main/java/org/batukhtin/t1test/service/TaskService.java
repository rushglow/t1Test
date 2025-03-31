package org.batukhtin.t1test.service;

import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.model.TaskEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface TaskService {
    TaskEntity createTask(TaskDto taskDto);

    Optional<TaskEntity> getTask(Long taskId);

    TaskEntity updateTask(Long id, TaskDto taskDto);

    String deleteTask(Long id);

    List<TaskEntity> getAllTasks();
}

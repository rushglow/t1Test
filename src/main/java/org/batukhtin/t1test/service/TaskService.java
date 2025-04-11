package org.batukhtin.t1test.service;

import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.dto.TaskRs;
import org.batukhtin.t1test.model.enums.TaskStatus;

import java.util.List;


public interface TaskService {
    TaskRs createTask(TaskDto taskDto);

    TaskRs getTask(Long taskId);

    TaskRs updateTask(Long taskId, TaskDto taskDto);

    void deleteTask(Long taskId);

    List<TaskRs> getAllTasks();

    TaskRs updateTaskStatus(Long taskId, TaskDto taskDto);
}

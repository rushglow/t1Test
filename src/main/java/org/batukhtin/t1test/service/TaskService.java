package org.batukhtin.t1test.service;

import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.dto.TaskRs;

import java.util.List;


public interface TaskService {
    TaskRs createTask(TaskDto taskDto);

    TaskRs getTask(Long taskId);

    TaskRs updateTask(Long id, TaskDto taskDto);

    void deleteTask(Long id);

    List<TaskRs> getAllTasks();
}

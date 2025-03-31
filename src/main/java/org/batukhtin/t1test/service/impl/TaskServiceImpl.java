package org.batukhtin.t1test.service.impl;

import lombok.AllArgsConstructor;
import org.batukhtin.t1test.aspect.annotation.LogException;
import org.batukhtin.t1test.aspect.annotation.LogExecution;
import org.batukhtin.t1test.aspect.annotation.PerfomanceTracking;
import org.batukhtin.t1test.context.UserContext;
import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.exception.ResourceNotFoundException;
import org.batukhtin.t1test.model.TaskEntity;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.repository.TaskRepository;
import org.batukhtin.t1test.repository.UserRepository;
import org.batukhtin.t1test.service.TaskService;
import org.batukhtin.t1test.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserContext userContext;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @LogException
    @LogExecution
    public TaskEntity createTask(TaskDto taskDto) throws RuntimeException {
        Optional<UserEntity> userOptional = userRepository.findUserByUsername(userContext.getCurrentUser().get().getUsername());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        UserEntity user = userOptional.get();
        TaskEntity taskEntity = new TaskEntity().builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .user(user)
                .build();
        return taskRepository.save(taskEntity);
    }

    @Override
    @LogException
    public Optional<TaskEntity> getTask(Long taskId) throws RuntimeException {
        Optional<UserEntity> userOptional = userRepository.findUserByUsername(userContext.getCurrentUser().get().getUsername());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        Long userId = userOptional.get().getId();
        Optional<TaskEntity> taskOptional = taskRepository.findByIdAndUserId(taskId, userId);
        if (taskOptional.isEmpty()) {
            throw new ResourceNotFoundException("Task not found");
        }
        return taskOptional;
    }

    @Override
    @Transactional
    @LogException
    public String deleteTask(Long taskId) throws RuntimeException {
        Optional<UserEntity> userOptional = userRepository.findUserByUsername(userContext.getCurrentUser().get().getUsername());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        Long userId = userOptional.get().getId();
        Optional<TaskEntity> taskOptional = taskRepository.findByIdAndUserId(taskId, userId);
        if (taskOptional.isEmpty()) {
            return "Task deleted";
        }
        taskRepository.deleteById(taskId);
        return "Task deleted";
    }

    @Override
    @PerfomanceTracking
    @LogException
    public List<TaskEntity> getAllTasks() throws RuntimeException {
        Optional<UserEntity> userOptional = userRepository.findUserByUsername(userContext.getCurrentUser().get().getUsername());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        Long userId = userOptional.get().getId();
        return taskRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    @LogException
    public TaskEntity updateTask(Long taskId, TaskDto taskDto) throws RuntimeException {
        Optional<UserEntity> userOptional = userRepository.findUserByUsername(userContext.getCurrentUser().get().getUsername());
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        Long userId = userOptional.get().getId();
        Optional<TaskEntity> taskOptional = taskRepository.findByIdAndUserId(taskId, userId);
        if (taskOptional.isEmpty()) {
            throw new ResourceNotFoundException("Task not found");
        }

        try {
            TaskEntity taskEntity = taskOptional.get();
            taskEntity.setTitle(taskDto.getTitle());
            taskEntity.setDescription(taskDto.getDescription());
            return taskRepository.save(taskEntity);
        } catch (NullPointerException e) {
            throw new RuntimeException("Task title or description is null");
        }
    }
}

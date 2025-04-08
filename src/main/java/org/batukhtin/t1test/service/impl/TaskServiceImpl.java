package org.batukhtin.t1test.service.impl;

import lombok.AllArgsConstructor;
import org.batukhtin.t1test.aspect.annotation.LogException;
import org.batukhtin.t1test.aspect.annotation.LogExecution;
import org.batukhtin.t1test.aspect.annotation.PerfomanceTracking;
import org.batukhtin.t1test.context.UserContext;
import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.dto.TaskRs;
import org.batukhtin.t1test.exception.ResourceNotFoundException;
import org.batukhtin.t1test.mapper.TaskMapper;
import org.batukhtin.t1test.model.TaskEntity;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.repository.TaskRepository;
import org.batukhtin.t1test.repository.UserRepository;
import org.batukhtin.t1test.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserContext userContext;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    @LogException
    @LogExecution
    public TaskRs createTask(TaskDto taskDto) throws RuntimeException {
        UserEntity user = userRepository.findUserByUsername(userContext.getCurrentUser().get().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return taskMapper.toTaskRs(taskRepository.save(taskMapper.toEntity(taskDto, user)));
    }

    @Override
    @LogException
    public TaskRs getTask(Long taskId) throws RuntimeException {
        Long userId = userRepository.findUserByUsername(userContext.getCurrentUser().get().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getId();

        TaskEntity taskEntity = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return taskMapper.toTaskRs(taskEntity);
    }

    @Override
    @Transactional
    @LogException
    public void deleteTask(Long taskId) throws RuntimeException {
        Long userId = userRepository.findUserByUsername(userContext.getCurrentUser().get().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getId();

        taskRepository.deleteById(taskId);
    }

    @Override
    @PerfomanceTracking
    @LogException
    public List<TaskRs> getAllTasks() throws RuntimeException {
        Long userId = userRepository.findUserByUsername(userContext.getCurrentUser().get().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getId();

        return taskRepository.findAllByUserId(userId).stream()
                .map(taskEntity -> taskMapper.toTaskRs(taskEntity))
                .toList();
    }

    @Override
    @Transactional
    @LogException
    public TaskRs updateTask(Long taskId, TaskDto taskDto) throws RuntimeException {
        Long userId = userRepository.findUserByUsername(userContext.getCurrentUser().get().getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")).getId();

        TaskEntity taskEntity = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        try {
            taskEntity.setTitle(taskDto.getTitle());
            taskEntity.setDescription(taskDto.getDescription());
            return taskMapper.toTaskRs(taskRepository.save(taskEntity));
        } catch (NullPointerException e) {
            throw new RuntimeException("Task title or description is null");
        }
    }
}

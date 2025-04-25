package org.batukhtin.t1test.service.impl;

import org.batukhtin.t1test.context.UserContext;
import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.dto.TaskRs;
import org.batukhtin.t1test.exception.ResourceNotFoundException;
import org.batukhtin.t1test.mapper.TaskMapper;
import org.batukhtin.t1test.model.TaskEntity;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.model.enums.TaskStatus;
import org.batukhtin.t1test.repository.TaskRepository;
import org.batukhtin.t1test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserContext userContext;
    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;


    private UserEntity userEntity;
    private TaskEntity taskEntity;
    private TaskDto taskDto;
    private TaskRs taskRs;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();

        taskEntity = new TaskEntity();
        taskEntity.setTitle("task title");
        taskEntity.setDescription("task description");
        taskEntity.setUser(userEntity);
        taskEntity.setStatus(TaskStatus.DONE);

        taskDto = new TaskDto();
        taskDto.setTitle("task title");
        taskDto.setDescription("task description");
        taskDto.setStatus(TaskStatus.DONE);

        taskRs = new TaskRs();
        taskRs.setTitle("task title");
        taskRs.setDescription("task description");
        taskRs.setStatus(TaskStatus.DONE);

        when(userContext.getCurrentUser()).thenReturn(Optional.of(userEntity));
        when(userRepository.findUserByUsername(any())).thenReturn(Optional.of(userEntity));
    }

    @Test
    @DisplayName("Создание таски")
    void createTask() {
        when(taskMapper.toEntity(taskDto, userEntity)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toTaskRs(taskEntity)).thenReturn(taskRs);

        TaskRs result = taskService.createTask(taskDto);
        assertNotNull(result);
        assertEquals(taskRs.getId(), result.getId());
    }

    @Test
    @DisplayName("Попытка создания таски без userContext")
    void createTask_withoutUserContext() {
        when(userRepository.findUserByUsername(any())).thenThrow(new ResourceNotFoundException("User not found"));

        assertThrows(ResourceNotFoundException.class, () -> taskService.createTask(taskDto));
        verify(userRepository).findUserByUsername(any());
        verify(taskRepository, never()).save(taskEntity);
    }

    @Test
    @DisplayName("Получение таски по ID")
    void getTask() {
        when(userRepository.findUserByUsername(any())).thenReturn(Optional.of(userEntity));
        when(taskRepository.findByIdAndUserId(1L, userEntity.getId())).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toTaskRs(taskEntity)).thenReturn(taskRs);

        TaskRs result = taskService.getTask(1L);
        assertNotNull(result);
        assertEquals(taskRs.getId(), result.getId());
        assertEquals(taskRs.getUserId(), result.getUserId());
    }

    @Test
    @DisplayName("Попытка получения несуществующей таски")
    void getTask_notFoundException() {
        when(userRepository.findUserByUsername(any())).thenReturn(Optional.of(userEntity));
        when(taskRepository.findByIdAndUserId(1L, userEntity.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTask(1L));
        verify(userRepository).findUserByUsername(any());
    }

    @Test
    @DisplayName("Попытка получения таски без userContext")
    void getTask_withoutUserContext() {
        when(userRepository.findUserByUsername(any())).thenThrow(new ResourceNotFoundException("User not found"));

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTask(1L));
        verify(userRepository).findUserByUsername(any());
        verify(taskRepository, never()).findByIdAndUserId(any(), any());
    }

    @Test
    void testDeleteTask_success() {
        when(userRepository.findUserByUsername(any())).thenReturn(Optional.of(userEntity));
        doNothing().when(taskRepository).deleteByIdAndUserId(1L, userEntity.getId());
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteByIdAndUserId(1L, userEntity.getId());
    }

    @Test
    @DisplayName("Попытка удаления таски без userContext")
    void testDeleteTask_withoutUserContext() {
        when(userRepository.findUserByUsername(any())).thenThrow(new ResourceNotFoundException("User not found"));

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L));
        verify(userRepository).findUserByUsername(any());
        verify(taskRepository, never()).findByIdAndUserId(any(), any());
    }

    @Test
    void testUpdateTask_success() {
        when(taskRepository.findByIdAndUserId(1L, userEntity.getId())).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(any())).thenReturn(taskEntity);
        when(taskMapper.toTaskRs(taskEntity)).thenReturn(taskRs);

        TaskRs result = taskService.updateTask(1L, taskDto);
        assertNotNull(result);
        verify(taskRepository).save(any());
    }

    @Test
    void testUpdateTask_throwsOnNullTitleOrDesc() {
        taskDto.setTitle(null);

        when(taskRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(taskEntity));

        assertThrows(RuntimeException.class, () -> taskService.updateTask(1L, taskDto));
    }
}
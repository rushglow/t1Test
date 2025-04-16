package org.batukhtin.t1test.mapper;

import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.dto.TaskRs;
import org.batukhtin.t1test.model.TaskEntity;
import org.batukhtin.t1test.model.UserEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskEntity toEntity(TaskDto taskDto, UserEntity user) {
        return  TaskEntity.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .user(user)
                .build();
    }

    public TaskDto toDto(TaskEntity taskEntity) {
        return TaskDto.builder()
                .title(taskEntity.getTitle())
                .description(taskEntity.getDescription())
                .build();
    }

    public TaskRs toTaskRs(TaskEntity taskEntity) {
        return TaskRs.builder()
                .id(taskEntity.getId())
                .title(taskEntity.getTitle())
                .description(taskEntity.getDescription())
                .status(taskEntity.getStatus())
                .userId(taskEntity.getUser().getId())
                .build();
    }
}

package org.batukhtin.t1test.service.impl;

import org.batukhtin.t1test.containers.KafkaContainer;
import org.batukhtin.t1test.context.UserContext;
import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.dto.TaskRs;
import org.batukhtin.t1test.dto.TaskStatusUpdateMessage;
import org.batukhtin.t1test.mapper.TaskMapper;
import org.batukhtin.t1test.model.TaskEntity;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.model.enums.TaskStatus;
import org.batukhtin.t1test.repository.TaskRepository;
import org.batukhtin.t1test.repository.UserRepository;
import org.batukhtin.t1test.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
public class TaskServiceImplSpringTest extends KafkaContainer {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserContext userContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private KafkaTemplate<String, TaskStatusUpdateMessage> kafkaTemplate;

    private UserEntity userEntity;
    private TaskEntity taskEntity;
    private TaskDto taskDto;
    private TaskRs taskRs;


    @BeforeEach
    void setUp() {
        userEntity = new UserEntity().builder()
                .id(1L)
                .username("test")
                .mail("test@test.com")
                .password("test")
                .role("ROLE_USER")
                .build();

        taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setTitle("test title");
        taskEntity.setDescription("test description");
        taskEntity.setStatus(TaskStatus.NEW);
        taskEntity.setUser(userEntity);

        taskRepository.deleteAll();
        userRepository.deleteAll();
        userEntity = userRepository.save(userEntity);
        taskRepository.save(taskEntity);
    }

    @Test
    void updateTaskStatus() {

        UserEntity user = userRepository.findUserByUsername(userEntity.getUsername()).get();

        TaskEntity task = taskRepository.findById(1L).get();

        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("new test title");
        taskDto.setDescription("new test description");
        taskDto.setStatus(TaskStatus.DONE);

        task.setStatus(taskDto.getStatus());
        taskEntity = taskRepository.save(task);

        TaskStatusUpdateMessage message = new TaskStatusUpdateMessage(
                taskEntity.getId(),
                taskEntity.getTitle(),
                taskEntity.getStatus(),
                taskEntity.getUser().getMail()
        );

        kafkaTemplate.send("t1_demo_email_send", message);

    }
}

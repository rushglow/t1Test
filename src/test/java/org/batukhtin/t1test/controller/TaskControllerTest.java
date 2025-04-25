package org.batukhtin.t1test.controller;

import org.batukhtin.t1test.containers.KafkaContainer;
import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.model.TaskEntity;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.model.enums.TaskStatus;
import org.batukhtin.t1test.repository.TaskRepository;
import org.batukhtin.t1test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Transactional
class TaskControllerTest extends KafkaContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private Long existingTaskId;

    @BeforeEach
    void setUp() throws Exception {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity().builder()
                .id(1L)
                .username("test")
                .mail("test@test.com")
                .password("test")
                .role("ROLE_USER")
                .build();
        user = userRepository.save(user);

        TaskEntity task = new TaskEntity();
        task.setTitle("test title");
        task.setDescription("test description");
        task.setStatus(TaskStatus.NEW);
        task.setUser(user);
        task = taskRepository.save(task);

        existingTaskId = task.getId();

        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetTask_success() throws Exception {
        mockMvc.perform(get("/tasks/" + existingTaskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample task"));
    }

    @Test
    void testGetTask_notFound() throws Exception {
        mockMvc.perform(get("/tasks/9999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testCreateTask_success() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTitle("New task");
        dto.setDescription("New task description");
        dto.setStatus(TaskStatus.NEW);

        mockMvc.perform(post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New task"));
    }

    @Test
    void testUpdateTask_success() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTitle("Updated title");
        dto.setDescription("Updated description");
        dto.setStatus(TaskStatus.IN_PROGRESS);

        mockMvc.perform(put("/tasks/" + existingTaskId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"));
    }

    @Test
    void testUpdateTask_notFound() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setTitle("Updated title");
        dto.setDescription("Updated description");
        dto.setStatus(TaskStatus.IN_PROGRESS);

        mockMvc.perform(put("/tasks/9999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeleteTask_success() throws Exception {
        mockMvc.perform(delete("/tasks/" + existingTaskId))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteTask_notFound() throws Exception {
        mockMvc.perform(delete("/tasks/9999"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAllTasks_success() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testUpdateTaskStatus_success() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setStatus(TaskStatus.DONE);

        mockMvc.perform(put("/tasks/" + existingTaskId + "/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void testUpdateTaskStatus_notFound() throws Exception {
        TaskDto dto = new TaskDto();
        dto.setStatus(TaskStatus.DONE);

        mockMvc.perform(put("/tasks/9999/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is4xxClientError());
    }
}
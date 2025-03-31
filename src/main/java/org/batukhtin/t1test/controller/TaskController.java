package org.batukhtin.t1test.controller;

import lombok.RequiredArgsConstructor;
import org.batukhtin.t1test.context.UserContext;
import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.model.TaskEntity;
import org.batukhtin.t1test.model.UserEntity;
import org.batukhtin.t1test.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    @PostMapping()
    public ResponseEntity<TaskEntity> createTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.createTask(taskDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(id).get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskEntity> updateTask(@RequestBody TaskDto taskDto, @PathVariable Long id) {
        return ResponseEntity.ok(taskService.updateTask(id,taskDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    @GetMapping()
    public List<TaskEntity> getAll() {
        return taskService.getAllTasks();
    }
}

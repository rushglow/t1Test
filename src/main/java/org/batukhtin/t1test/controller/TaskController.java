package org.batukhtin.t1test.controller;

import lombok.RequiredArgsConstructor;
import org.batukhtin.t1test.context.UserContext;
import org.batukhtin.t1test.dto.TaskDto;
import org.batukhtin.t1test.dto.TaskRs;
import org.batukhtin.t1test.model.enums.TaskStatus;
import org.batukhtin.t1test.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TaskRs createTask(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskRs getTask(@PathVariable Long taskId) {
        return taskService.getTask(taskId);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TaskRs> getAll() {
        return taskService.getAllTasks();
    }

    @PutMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public TaskRs updateTask(@RequestBody TaskDto taskDto, @PathVariable Long taskId) {
        return taskService.updateTask(taskId,taskDto);
    }

    @PutMapping("/{taskId}/status")
    @ResponseStatus(HttpStatus.OK)
    public TaskRs updateTaskStatus(@PathVariable Long taskId, @RequestBody TaskDto taskDto) {
        return taskService.updateTaskStatus(taskId, taskDto);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }

}

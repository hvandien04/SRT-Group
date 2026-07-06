package com.example.todolist.controller;

import com.example.todolist.dto.request.CreateTaskRequest;
import com.example.todolist.dto.request.UpdateTaskRequest;
import com.example.todolist.dto.response.PagedResponse;
import com.example.todolist.dto.response.TaskResponse;
import com.example.todolist.dto.response.TaskStatsResponse;
import com.example.todolist.entity.User;
import com.example.todolist.service.TaskService;
import com.example.todolist.wapper.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Create a new task
     */
    @PostMapping
    public ApiResponse<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication) {
        User user = extractUserFromAuth(authentication);
        TaskResponse response = taskService.createTask(user, request);
        return ApiResponse.ok(response);
    }

    /**
     * Get all tasks with pagination
     */
    @GetMapping
    public ApiResponse<PagedResponse<TaskResponse>> getAllTasks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        User user = extractUserFromAuth(authentication);
        PagedResponse<TaskResponse> response = taskService.getAllTasks(user, page, size);
        return ApiResponse.ok(response);
    }

    /**
     * Get tasks by status with pagination
     */
    @GetMapping("/filter")
    public ApiResponse<PagedResponse<TaskResponse>> getTasksByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        User user = extractUserFromAuth(authentication);
        PagedResponse<TaskResponse> response = taskService.getTasksByStatus(user, status, page, size);
        return ApiResponse.ok(response);
    }

    /**
     * Get a specific task
     */
    @GetMapping("/{taskId}")
    public ApiResponse<TaskResponse> getTask(
            @PathVariable Long taskId,
            Authentication authentication) {
        User user = extractUserFromAuth(authentication);
        TaskResponse response = taskService.getTask(user, taskId);
        return ApiResponse.ok(response);
    }

    /**
     * Update a task
     */
    @PutMapping("/{taskId}")
    public ApiResponse<TaskResponse> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequest request,
            Authentication authentication) {
        User user = extractUserFromAuth(authentication);
        TaskResponse response = taskService.updateTask(user, taskId, request);
        return ApiResponse.ok(response);
    }

    /**
     * Delete a task
     */
    @DeleteMapping("/{taskId}")
    public ApiResponse<String> deleteTask(
            @PathVariable Long taskId,
            Authentication authentication) {
        User user = extractUserFromAuth(authentication);
        taskService.deleteTask(user, taskId);
        return ApiResponse.ok("Task deleted successfully");
    }

    /**
     * Toggle task status between PENDING and COMPLETED
     */
    @PatchMapping("/{taskId}/toggle")
    public ApiResponse<TaskResponse> toggleTaskStatus(
            @PathVariable Long taskId,
            Authentication authentication) {
        User user = extractUserFromAuth(authentication);
        TaskResponse response = taskService.toggleTaskStatus(user, taskId);
        return ApiResponse.ok(response);
    }

    /**
     * Get task statistics
     */
    @GetMapping("/stats/overview")
    public ApiResponse<TaskStatsResponse> getTaskStats(
            Authentication authentication) {
        User user = extractUserFromAuth(authentication);
        TaskStatsResponse response = taskService.getTaskStats(user);
        return ApiResponse.ok(response);
    }

    // Helper method to extract user from authentication
    private User extractUserFromAuth(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getClaimAsString("sub");
        
        // Create a User object with just the ID for authorization checks
        User user = new User();
        user.setId(Long.parseLong(userId));
        return user;
    }
}

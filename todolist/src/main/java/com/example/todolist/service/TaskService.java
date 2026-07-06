package com.example.todolist.service;

import com.example.todolist.constant.TaskStatus;
import com.example.todolist.dto.request.CreateTaskRequest;
import com.example.todolist.dto.request.UpdateTaskRequest;
import com.example.todolist.dto.response.PagedResponse;
import com.example.todolist.dto.response.TaskResponse;
import com.example.todolist.dto.response.TaskStatsResponse;
import com.example.todolist.entity.Task;
import com.example.todolist.entity.User;
import com.example.todolist.exception.ErrorCodeApplication;
import com.example.todolist.wapper.AppException;
import com.example.todolist.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;


    public TaskResponse createTask(User user, CreateTaskRequest request) {
        Task task = Task.builder()
                .user(user)
                .title(request.getTitle().trim())
                .description(request.getDescription() != null ? request.getDescription().trim() : null)
                .status(TaskStatus.PENDING)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }


    public PagedResponse<TaskResponse> getAllTasks(User user, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Task> tasks = taskRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return mapToPagedResponse(tasks);
    }

    public PagedResponse<TaskResponse> getTasksByStatus(User user, String status, int pageNumber, int pageSize) {
        validateStatus(status);
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Task> tasks = taskRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status, pageable);
        return mapToPagedResponse(tasks);
    }

    public TaskResponse getTask(User user, Long taskId) {
        Task task = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new AppException(ErrorCodeApplication.TASK_NOT_FOUND));
        return mapToResponse(task);
    }


    public TaskResponse updateTask(User user, Long taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new AppException(ErrorCodeApplication.TASK_NOT_FOUND));

        task.setTitle(request.getTitle().trim());
        
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription().trim());
        }
        
        if (request.getStatus() != null) {
            validateStatus(request.getStatus());
            task.setStatus(request.getStatus());
        }
        
        task.setUpdatedAt(Instant.now());
        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    public void deleteTask(User user, Long taskId) {
        Task task = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new AppException(ErrorCodeApplication.TASK_NOT_FOUND));
        taskRepository.delete(task);
    }

    public TaskResponse toggleTaskStatus(User user, Long taskId) {
        Task task = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new AppException(ErrorCodeApplication.TASK_NOT_FOUND));

        String currentStatus = task.getStatus();
        String newStatus = TaskStatus.PENDING.equals(currentStatus) ? TaskStatus.COMPLETED : TaskStatus.PENDING;
        
        task.setStatus(newStatus);
        task.setUpdatedAt(Instant.now());
        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    public TaskStatsResponse getTaskStats(User user) {
        long totalTasks = taskRepository.countByUserAndStatus(user, TaskStatus.PENDING) +
                          taskRepository.countByUserAndStatus(user, TaskStatus.COMPLETED);
        long completedTasks = taskRepository.countByUserAndStatus(user, TaskStatus.COMPLETED);
        long pendingTasks = taskRepository.countByUserAndStatus(user, TaskStatus.PENDING);

        return TaskStatsResponse.builder()
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
                .build();
    }

    private void validateStatus(String status) {
        if (!status.equals(TaskStatus.PENDING) && !status.equals(TaskStatus.COMPLETED)) {
            throw new AppException(ErrorCodeApplication.INVALID_STATUS);
        }
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }

    private PagedResponse<TaskResponse> mapToPagedResponse(Page<Task> page) {
        return PagedResponse.<TaskResponse>builder()
                .content(page.getContent().stream()
                        .map(this::mapToResponse)
                        .toList())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLastPage(page.isLast())
                .build();
    }

}

package com.example.todolist.repository;

import com.example.todolist.entity.Task;
import com.example.todolist.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndUser(Long id, User user);

    Page<Task> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Task> findByUserAndStatusOrderByCreatedAtDesc(User user, String status, Pageable pageable);

    long countByUserAndStatus(User user, String status);
}

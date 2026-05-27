package com.taskflow.taskflow.repository;

import com.taskflow.taskflow.entity.Task;
import com.taskflow.taskflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);

    boolean existsByUser(User user);
}
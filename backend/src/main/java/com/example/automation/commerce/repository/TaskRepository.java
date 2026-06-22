package com.example.automation.commerce.repository;

import com.example.automation.commerce.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}

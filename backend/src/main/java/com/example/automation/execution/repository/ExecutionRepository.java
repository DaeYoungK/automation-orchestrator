package com.example.automation.execution.repository;

import com.example.automation.execution.domain.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionRepository extends JpaRepository<Execution, Long> {
}

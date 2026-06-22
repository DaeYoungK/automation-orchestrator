package com.example.automation.commerce.repository;

import com.example.automation.commerce.domain.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutionRepository extends JpaRepository<Execution, Long> {
}

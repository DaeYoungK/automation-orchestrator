package com.example.automation.commerce.domain;

import com.example.automation.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "execution_log")
@NoArgsConstructor
public class ExecutionLog extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "execution_log_id")
    private Long id;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_id")
    private Execution execution;

    @Enumerated(value = EnumType.STRING)
    private LogLevel logLevel;

    protected ExecutionLog(String message, LogLevel logLevel) {
        this.message = message;
        this.logLevel = logLevel;
    }

    protected void assignExecution(Execution execution) {
        this.execution = execution;
    }

    public static ExecutionLog create(String message, LogLevel logLevel) {
        return new ExecutionLog(message, logLevel);
    }

}

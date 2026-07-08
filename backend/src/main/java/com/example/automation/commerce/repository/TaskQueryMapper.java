package com.example.automation.commerce.repository;

import com.example.automation.commerce.domain.TaskStatus;
import com.example.automation.commerce.dto.TaskSummaryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskQueryMapper {
    List<TaskSummaryResponse> findTasks(TaskStatus taskStatus);
}

package com.example.automation.task.repository;

import com.example.automation.task.domain.TaskStatus;
import com.example.automation.task.dto.TaskSummaryResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskQueryMapper {
    List<TaskSummaryResponse> findTasks(TaskStatus taskStatus);
}

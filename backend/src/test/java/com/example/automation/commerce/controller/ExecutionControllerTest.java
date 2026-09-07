package com.example.automation.commerce.controller;

import com.example.automation.commerce.domain.ExecutionStatus;
import com.example.automation.commerce.service.ExecutionRunService;
import com.example.automation.executor.dto.ExecutionRunResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExecutionController.class)
class ExecutionControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean ExecutionRunService executionRunService;

    @Test
    void 실행_API_호출시_SUCCESS_결과를_200과_JSON으로_반환한다() throws Exception {
        //given
        Long executionId = 1L;
        ExecutionRunResult result = new ExecutionRunResult(executionId, ExecutionStatus.SUCCESS, false, null);
        when(executionRunService.runExecution(executionId))
                .thenReturn(result);

        //when & then
        mockMvc.perform(post("/api/executions/{executionId}/run", executionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.executionId").value(executionId))
                .andExpect(jsonPath("$.executionStatus").value(ExecutionStatus.SUCCESS.name()))
                .andExpect(jsonPath("$.retryable").value(false))
                .andExpect(jsonPath("$.failureMessage").value(nullValue()));
        verify(executionRunService).runExecution(executionId);
    }

    @Test
    void 실행_API_호출시_FAILED_결과를_200과_JSON으로_반환한다() throws Exception {
        //given
        Long executionId = 1L;
        String failureMessage = "API 호출 실패";
        ExecutionRunResult result = new ExecutionRunResult(executionId, ExecutionStatus.FAILED, true, failureMessage);
        when(executionRunService.runExecution(executionId))
                .thenReturn(result);

        //when & then
        mockMvc.perform(post("/api/executions/{executionId}/run", executionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.executionId").value(executionId))
                .andExpect(jsonPath("$.executionStatus").value(ExecutionStatus.FAILED.name()))
                .andExpect(jsonPath("$.retryable").value(true))
                .andExpect(jsonPath("$.failureMessage").value(failureMessage));
        verify(executionRunService).runExecution(executionId);
    }
}
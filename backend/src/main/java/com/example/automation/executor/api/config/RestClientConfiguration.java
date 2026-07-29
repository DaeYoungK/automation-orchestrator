package com.example.automation.executor.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient apiExecutionClient(RestClient.Builder builder) {
        return builder.build();
    }
}

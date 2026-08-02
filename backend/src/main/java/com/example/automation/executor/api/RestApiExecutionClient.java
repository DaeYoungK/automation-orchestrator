package com.example.automation.executor.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class RestApiExecutionClient implements ApiExecutionClient{

    private final RestClient restClient;

    @Override
    public ApiExecutionResponse execute(ApiExecutionRequest request) {

        return restClient.method(request.httpMethod())
                .uri(request.url())
                .headers(httpHeaders -> request.headers()
                                    .forEach((k, v) -> httpHeaders.set(k,v))
                        )
                .body(request.body())
                .exchange((clientRequest, clientResponse) -> {
                    int statusCode = clientResponse.getStatusCode().value();
                    String body = new String(
                            clientResponse.getBody().readAllBytes(), StandardCharsets.UTF_8);

                    if (clientResponse.getStatusCode().is2xxSuccessful()) {
                        return new ApiExecutionResponse(statusCode, body);
                    }
                    throw new RuntimeException(
                            "API 호출 실패: " + statusCode + "\n" +
                                    "body = " + body
                    );
                });
    }
}

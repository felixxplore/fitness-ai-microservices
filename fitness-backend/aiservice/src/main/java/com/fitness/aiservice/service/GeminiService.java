package com.fitness.aiservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public GeminiService(WebClient.Builder webClientBuilder ) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String question) {
        // Build the Gemini API request body
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", question)
                        })
                }
        );

        try {
            // Call Gemini API synchronously (blocking) since we're in a RabbitMQ listener
            // blocking call inside message consumer is fine

            return webClient.post()
                    .uri(apiUrl)
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key",apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            log.error("Gemini API call failed: {}", e.getMessage());
            return "Unable to generate recommendation at this time.";
        }
    }


}

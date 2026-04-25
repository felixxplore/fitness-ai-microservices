package com.fitness.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final WebClient webClient;

    public Mono<Boolean> validateUser(String userId){
             return webClient
                    .get()
                    .uri("http://USER-SERVICE/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .onErrorResume(WebClientResponseException.class, ex -> {
                        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                            return Mono.error(new RuntimeException("User not found with this id : " + userId));
                        } else if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                            return Mono.error(new RuntimeException("Invalid user id : " + userId));
                        }
                        return Mono.error(new RuntimeException("Unexpected error : " + ex.getMessage()));
                    });

    }

    public Mono<UserResponse> registerUser(RegisterRequest registerRequest) {
        log.info("Calling registration api for user email :{}",registerRequest.getEmail());
        return webClient
                .post()
                .uri("/api/users/register")
                .bodyValue(registerRequest)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new RuntimeException("Bad Request "+ex.getMessage()));
                    } else if (ex.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                        return Mono.error(new RuntimeException("Internal server Error "+ex.getMessage()));
                    }
                    return Mono.error(new RuntimeException("Unexpected error : " + ex.getMessage()));
                });
    }
}

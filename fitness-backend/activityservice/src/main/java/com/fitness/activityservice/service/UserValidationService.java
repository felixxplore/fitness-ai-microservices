package com.fitness.activityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

    private final WebClient.Builder webClient;

    public boolean validateUser(String userId){
        try{
            return webClient
                    .build()
                    .get()
                    .uri("http://USER-SERVICE/api/users/{userId}/validate",userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        }catch (WebClientResponseException ex){
            if(ex.getStatusCode()== HttpStatus.NOT_FOUND){
                throw new RuntimeException("User not found with this id : "+ userId);
            }
            else if(ex.getStatusCode()==HttpStatus.BAD_REQUEST){
                throw new RuntimeException("Invalid user id : "+ userId);
            }
        }

        return false;
    }
}

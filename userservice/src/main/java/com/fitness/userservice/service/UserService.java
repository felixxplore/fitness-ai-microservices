package com.fitness.userservice.service;


import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KeycloakService keycloakService;

    public UserResponse getUserProfile(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse userResponse=new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setKeycloakId(user.getKeycloakId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {

        log.info("User service register API called");

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("User already exists");
        }

        String keycloakId = keycloakService.createUser(request);

        if(keycloakId == null){
            throw new RuntimeException("Error creating user in Keycloak");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setKeycloakId(keycloakId);

        User savedUser = userRepository.saveAndFlush(user);

        return mapToResponse(savedUser);
    }
    public Boolean existByUserId(String userId) {
        log.info("Calling User validation api for userId : {}",userId);
        return userRepository.existsByKeycloakId(userId);
    }

    private UserResponse mapToResponse(User user){

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setKeycloakId(user.getKeycloakId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }
}

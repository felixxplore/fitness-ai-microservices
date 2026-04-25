package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KeycloakService {

    private Keycloak keycloak;


    public KeycloakService(){
        keycloak = KeycloakBuilder.builder()
                .serverUrl("http://keycloak:8080")  // ✅ bas ye ek line fix
                .realm("fitness-realm")
                .clientId("fitness-user-service")
                .clientSecret("dtnNeKyU2YBx8LaAp754KgGkwjGtzmcy")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }

    @Transactional
    public String createUser(RegisterRequest request){
        List<UserRepresentation> users =
                keycloak.realm("fitness-realm")
                        .users()
                        .search(request.getEmail());

        if (!users.isEmpty()) {
            throw new RuntimeException("User already exists in Keycloak");
        }

        UserRepresentation user = new UserRepresentation();

        user.setEmail(request.getEmail());
        user.setUsername(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(false);


        Response response = keycloak.realm("fitness-realm")
                .users()
                .create(user);

        String userId=CreatedResponseUtil.getCreatedId(response);

        // set password
        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(request.getPassword());
        password.setTemporary(false);

        keycloak.realm("fitness-realm")
                .users()
                .get(userId)
                .resetPassword(password);

//        // Send verification email
//        keycloak.realm("fitness-realm")
//                .users()
//                .get(userId)
//                .sendVerifyEmail();


        return userId;
    }
}

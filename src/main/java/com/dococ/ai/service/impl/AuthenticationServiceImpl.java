package com.dococ.ai.service.impl;

import com.dococ.ai.entity.User;
import com.dococ.ai.exceptions.UnauthorizedAccessException;
import com.dococ.ai.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private RestTemplate restTemplate;

   private final String validationUrl = "http://iam-service.default.svc.cluster.local:8081/api/v1/auth/token/validate";

    //private final String validationUrl = "http://localhost:8081/api/v1/auth/token/validate";

    private User validateRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<User> user = restTemplate.exchange(validationUrl, HttpMethod.GET, entity, User.class);

        return user.getBody();
    }

    @Override
    public void validateRole(String token, List<String> roles) {
        User user = validateRequest(token);
        if (!roles.contains(user.getRole())) {
            throw new UnauthorizedAccessException("You are not authorized to do this action");
        }
    }
}

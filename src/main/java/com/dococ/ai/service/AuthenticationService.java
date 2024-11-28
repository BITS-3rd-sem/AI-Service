package com.dococ.ai.service;

import java.util.List;

public interface AuthenticationService {

    void validateRole(String token, List<String> roles);
}

package com.evertix.financialwallet.service;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.security.request.LoginRequest;
import com.evertix.financialwallet.security.request.SignUpRequest;
import com.evertix.financialwallet.security.response.JwtResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<MessageResponse> registerUser(SignUpRequest signUpRequest);
    JwtResponse authenticationUser(LoginRequest loginRequest);
}

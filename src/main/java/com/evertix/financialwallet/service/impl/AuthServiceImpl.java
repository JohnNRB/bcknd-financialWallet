package com.evertix.financialwallet.service.impl;

import com.evertix.financialwallet.controller.commons.MessageResponse;
import com.evertix.financialwallet.controller.constants.ResponseConstants;
import com.evertix.financialwallet.model.Role;
import com.evertix.financialwallet.model.User;
import com.evertix.financialwallet.model.emuns.ERole;
import com.evertix.financialwallet.repository.RoleRepository;
import com.evertix.financialwallet.repository.UserRepository;
import com.evertix.financialwallet.security.jwt.JWTUtils;
import com.evertix.financialwallet.security.request.LoginRequest;
import com.evertix.financialwallet.security.request.SignUpRequest;
import com.evertix.financialwallet.security.response.JwtResponse;
import com.evertix.financialwallet.security.service.UserDetailsImpl;
import com.evertix.financialwallet.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTUtils jwtUtils;

    @Override
    public ResponseEntity<MessageResponse> registerUser(SignUpRequest signUpRequest) {
        try {
            // Validate if Username Exists
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.
                        status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Username is already taken")
                                .build());
            }
            // Validate if Email Exists
            else if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.
                        status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.builder()
                                .code(ResponseConstants.ERROR_CODE)
                                .message("Email is already taken")
                                .build());
            }
            // Create a new User
            User createUser = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getEmail(), signUpRequest.getName(),
                    signUpRequest.getLastName(), signUpRequest.getDni(), signUpRequest.getPhone());
            // Choose Role User
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            // Save All
            createUser.setRole(userRole);
            userRepository.save(createUser);
            return ResponseEntity.
                    status(HttpStatus.CREATED).
                    body(MessageResponse.builder()
                            .code(ResponseConstants.SUCCESS_CODE)
                            .message("Successful Register")
                            .data(createUser)
                            .build());
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.builder()
                            .code(ResponseConstants.ERROR_CODE)
                            .message("Internal Error: " + sw.toString())
                            .build());
        }
    }

    @Override
    public JwtResponse authenticationUser(LoginRequest loginRequest) {
        // Auth username & password
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Get JWT Token
        String jwt = jwtUtils.generatedJwtToken(authentication);
        // Get User
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Convert Role in string
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String roleOne = "";
        for (String rol: roles){ roleOne = roleOne.concat(rol); }
        // Get All Data User
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roleOne, userDetails.getName(),
                userDetails.getLastName(), userDetails.getDni(), userDetails.getPhone());
    }
}

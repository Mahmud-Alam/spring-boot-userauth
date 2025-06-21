package com.mahmudalam.userauth.controller;

import com.mahmudalam.userauth.dto.LoginRequest;
import com.mahmudalam.userauth.dto.RegisterRequest;
import com.mahmudalam.userauth.dto.UserResponse;
import com.mahmudalam.userauth.model.User;
import com.mahmudalam.userauth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse<User>> register(@RequestBody RegisterRequest request) {
        UserResponse<User> response = authService.register(request);
        return response.isSuccess()
                ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse<String>> login(@RequestBody LoginRequest request) {
        UserResponse<String> response = authService.login(request.getUsername(), request.getPassword());
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}

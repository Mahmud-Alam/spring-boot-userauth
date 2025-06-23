package com.mahmudalam.userauth.service;

import com.mahmudalam.userauth.dto.request.LoginRequest;
import com.mahmudalam.userauth.dto.request.RegisterRequest;
import com.mahmudalam.userauth.dto.response.AuthResponse;
import com.mahmudalam.userauth.model.User;
import com.mahmudalam.userauth.repository.UserRepository;
import com.mahmudalam.userauth.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(request.getUsername());
                return new AuthResponse(true, token, null);
            }
        } catch (AuthenticationException e) {
            return new AuthResponse(false, null, "Invalid credentials.");
        }

        return new AuthResponse(false, null, "Authentication failed.");
    }

    public AuthResponse register(RegisterRequest request) {
        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                return new AuthResponse(false, null, "Username already exists.");
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                return new AuthResponse(false, null, "Email already exists.");
            }

            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            userRepository.save(user);

            String token = jwtService.generateToken(request.getUsername());

            return new AuthResponse(true, token, null);

        } catch (Exception e) {
            return new AuthResponse(false, null, "User registration failed.");
        }
    }
}
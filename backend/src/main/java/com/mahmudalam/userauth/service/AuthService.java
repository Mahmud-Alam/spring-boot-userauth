package com.mahmudalam.userauth.service;

import com.mahmudalam.userauth.dto.RegisterRequest;
import com.mahmudalam.userauth.dto.UserResponse;
import com.mahmudalam.userauth.model.User;
import com.mahmudalam.userauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse<User> register(RegisterRequest request) {
        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                return new UserResponse<>(false, null, "Username already exists");
            }

            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return new UserResponse<>(false, null, "Email already exists");
            }

            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole() != null ? request.getRole() : User.Role.USER)
                    .status(User.Status.ACTIVE)
                    .build();

            User savedUser = userRepository.save(user);
            return new UserResponse<>(true, savedUser, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Registration failed: " + e.getMessage());
        }
    }

    public UserResponse<String> login(String email, String rawPassword) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isEmpty()) {
                return new UserResponse<>(false, null, "Invalid email.");
            }

            User user = optionalUser.get();

            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                return new UserResponse<>(false, null, "Password is incorrect.");
            }

            return new UserResponse<>(true, "Login successful", null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Login failed: " + e.getMessage());
        }
    }

}

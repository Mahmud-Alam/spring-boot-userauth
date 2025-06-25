package com.mahmudalam.userauth.service;

import com.mahmudalam.userauth.dto.request.LoginRequest;
import com.mahmudalam.userauth.dto.request.RegisterRequest;
import com.mahmudalam.userauth.dto.response.AuthResponse;
import com.mahmudalam.userauth.model.RefreshToken;
import com.mahmudalam.userauth.model.User;
import com.mahmudalam.userauth.repository.RefreshTokenRepository;
import com.mahmudalam.userauth.repository.UserRepository;
import com.mahmudalam.userauth.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public AuthResponse login(LoginRequest request) {
        try {
            String requestUsername = request.getUsername().toLowerCase();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestUsername, request.getPassword()));

            if (authentication.isAuthenticated()) {
                User user = userRepository.findByUsername(requestUsername);

                // Delete existing refresh tokens for user
                refreshTokenRepository.deleteByUser(user);
                refreshTokenRepository.flush(); // <-- force delete to DB

                String accessToken = jwtService.generateAccessToken(user);
                String refreshTokenStr = jwtService.generateRefreshToken(user);

                RefreshToken refreshToken = RefreshToken.builder()
                        .token(refreshTokenStr)
                        .user(user)
                        .expiryDate(new Date(System.currentTimeMillis() + jwtService.getRefreshTokenValidity()))
                        .build();

                refreshTokenRepository.save(refreshToken);

                return new AuthResponse(true, accessToken, refreshTokenStr, null);
            }
        } catch (AuthenticationException e){
            return new AuthResponse(false,null, null,"Invalid credentials.");
        }

        return new AuthResponse(false, null, null, "Authentication failed.");
    }

    public AuthResponse register(RegisterRequest request) {
        try {
            String requestUsername = request.getUsername().toLowerCase();
            String requestEmail = request.getEmail().toLowerCase();
            if(userRepository.existsByUsername(requestUsername)){
                return new AuthResponse(false, null, null, "Username already exists.");
            }
            if(userRepository.existsByEmail(requestEmail)){
                return new AuthResponse(false, null, null, "Email already exists.");
            }

            User user = User.builder()
                    .username(requestUsername)
                    .email(requestEmail)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            userRepository.save(user);

            String accessToken = jwtService.generateAccessToken(user);
            String refreshTokenStr = jwtService.generateRefreshToken(user);

            RefreshToken refreshToken = RefreshToken.builder()
                    .token(refreshTokenStr)
                    .user(user)
                    .expiryDate(new Date(System.currentTimeMillis() + jwtService.getRefreshTokenValidity()))
                    .build();

            refreshTokenRepository.save(refreshToken);

            return new AuthResponse(true, accessToken, refreshTokenStr, null);

        } catch (Exception e) {
            return new AuthResponse(false, null, null, "User registration failed.");
        }
    }

    public AuthResponse refreshToken(String refreshTokenStr) {
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(refreshTokenStr);

        if (tokenOpt.isEmpty() || !jwtService.isRefreshTokenValid(refreshTokenStr)) {
            return new AuthResponse(false, null, null, "Invalid or expired refresh token.");
        }

        User user = tokenOpt.get().getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        return new AuthResponse(true, newAccessToken, null, null);
    }
}

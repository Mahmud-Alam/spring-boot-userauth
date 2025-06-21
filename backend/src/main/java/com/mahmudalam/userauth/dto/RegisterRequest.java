package com.mahmudalam.userauth.dto;

import com.mahmudalam.userauth.model.User;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private User.Role role;
}

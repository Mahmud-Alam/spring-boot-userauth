package com.mahmudalam.userauth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse<T> {
    private boolean success;
    private T data;
    private String error;
}

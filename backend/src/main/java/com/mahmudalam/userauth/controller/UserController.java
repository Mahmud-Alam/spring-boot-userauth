package com.mahmudalam.userauth.controller;

import com.mahmudalam.userauth.dto.response.UserResponse;
import com.mahmudalam.userauth.model.User;
import com.mahmudalam.userauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<UserResponse<List<User>>> getAllUsers() {
        UserResponse<List<User>> response = userService.getAllUsers();
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse<User>> getUserById(@PathVariable Long id){
        UserResponse<User> response = userService.getUserById(id);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserResponse<User>> createUser(@RequestBody User createdUser){
        UserResponse<User> response = userService.createUser(createdUser);
        return response.isSuccess()
                ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse<User>> putUpdateUser(@PathVariable Long id, @RequestBody User updatedUser){
        UserResponse<User> response = userService.putUpdateUser(id, updatedUser);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse<User>> patchUpdateUser(@PathVariable Long id, @RequestBody User updatedUser){
        UserResponse<User> response = userService.patchUpdateUser(id, updatedUser);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponse<User>> getProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse<User> response = userService.getUserByUsername(username);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponse<User>> putUpdateProfile(@RequestBody User updatedUser){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse<User> response = userService.putUpdateProfile(username, updatedUser);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PatchMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponse<User>> patchUpdateProfile(@RequestBody User updatedUser){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse<User> response = userService.patchUpdateProfile(username, updatedUser);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

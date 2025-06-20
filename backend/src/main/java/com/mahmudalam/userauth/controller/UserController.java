package com.mahmudalam.userauth.controller;

import java.util.List;

import com.mahmudalam.userauth.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mahmudalam.userauth.model.User;
import com.mahmudalam.userauth.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse<List<User>>> getAllUsers() {
        UserResponse<List<User>> response = userService.getAllUsers();
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse<User>> getUserById(@PathVariable Long id){
        UserResponse<User> response = userService.getUserById(id);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PostMapping
    public ResponseEntity<UserResponse<User>> createUser(@RequestBody User createdUser){
        UserResponse<User> response = userService.createUser(createdUser);
        return response.isSuccess()
                ? ResponseEntity.status(HttpStatus.CREATED).body(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse<User>> putUpdateUser(@PathVariable Long id, @RequestBody User updatedUser){
        UserResponse<User> response = userService.putUpdateUser(id, updatedUser);
        return response.isSuccess()
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}

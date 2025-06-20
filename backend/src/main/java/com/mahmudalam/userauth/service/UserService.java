package com.mahmudalam.userauth.service;

import java.util.List;

import com.mahmudalam.userauth.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mahmudalam.userauth.model.User;
import com.mahmudalam.userauth.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse<List<User>> getAllUsers(){
        try {
            List<User> users = userRepository.findAll();
            return new UserResponse<>(true, users, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to fetch users: " + e.getMessage());
        }
    }

    public UserResponse<User> getUserById(Long id){
        try{
            return userRepository.findById(id)
                    .map(user -> new UserResponse<>(true, user, null))
                    .orElse(new UserResponse<>(false, null, "User not found with ID: " + id));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to retrieve user: " + e.getMessage());
        }
    }

    public UserResponse<User> createUser(User createdUser){
        try{
            User created = userRepository.save(createdUser);
            return new UserResponse<>(true, created, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to create user: " + e.getMessage());
        }
    }

    public UserResponse<User> putUpdateUser(Long id, User updatedUser) {
        try{
            return userRepository.findById(id)
                    .map(existingUser -> {
                        existingUser.setUsername(updatedUser.getUsername());
                        existingUser.setEmail(updatedUser.getEmail());
                        existingUser.setPassword(updatedUser.getPassword());
                        existingUser.setFirstName(updatedUser.getFirstName());
                        existingUser.setLastName(updatedUser.getLastName());
                        existingUser.setPhone(updatedUser.getPhone());
                        existingUser.setDob(updatedUser.getDob());
                        existingUser.setGender(updatedUser.getGender());
                        existingUser.setAddress(updatedUser.getAddress());
                        existingUser.setRole(updatedUser.getRole());
                        existingUser.setStatus(updatedUser.getStatus());

                        userRepository.save(existingUser);

                        return new UserResponse<>(true, existingUser, null);
                    }).orElse(new UserResponse<>(false, null, "User not found to update"));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update user: " + e.getMessage());
        }
    }
}

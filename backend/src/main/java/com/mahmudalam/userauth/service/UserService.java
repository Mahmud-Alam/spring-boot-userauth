package com.mahmudalam.userauth.service;

import com.mahmudalam.userauth.dto.response.UserResponse;
import com.mahmudalam.userauth.model.User;
import com.mahmudalam.userauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return new UserResponse<>(true, users, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to fetch users: " + e.getMessage());
        }
    }

    public UserResponse<User> getUserById(Long id) {
        try {
            return userRepository.findById(id)
                    .map(user -> new UserResponse<>(true, user, null))
                    .orElse(new UserResponse<>(false, null, "User not found with ID: " + id));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to retrieve userdetails: " + e.getMessage());
        }
    }

    public UserResponse<User> createUser(User createdUser) {
        try {
            createdUser.setPassword(passwordEncoder.encode(createdUser.getPassword()));
            User created = userRepository.save(createdUser);
            return new UserResponse<>(true, created, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to create userdetails: " + e.getMessage());
        }
    }

    public UserResponse<User> putUpdateUser(Long id, User updatedUser) {
        try {
            return userRepository.findById(id)
                    .map(existingUser -> {
                        existingUser.setUsername(updatedUser.getUsername().toLowerCase());
                        existingUser.setEmail(updatedUser.getEmail());
                        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
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
            return new UserResponse<>(false, null, "Failed to update userdetails: " + e.getMessage());
        }
    }

    public UserResponse<User> patchUpdateUser(Long id, User updatedUser) {
        try {
            return userRepository.findById(id)
                    .map(existingUser -> {
                        existingUser.setUsername(updatedUser.getUsername() != null ? updatedUser.getUsername().toLowerCase() : existingUser.getUsername().toLowerCase());
                        existingUser.setEmail(updatedUser.getEmail() != null ? updatedUser.getEmail() : existingUser.getEmail());
                        existingUser.setPassword(updatedUser.getPassword() != null ? passwordEncoder.encode(updatedUser.getPassword()) : existingUser.getPassword());
                        existingUser.setFirstName(updatedUser.getFirstName() != null ? updatedUser.getFirstName() : existingUser.getFirstName());
                        existingUser.setLastName(updatedUser.getLastName() != null ? updatedUser.getLastName() : existingUser.getLastName());
                        existingUser.setPhone(updatedUser.getPhone() != null ? updatedUser.getPhone() : existingUser.getPhone());
                        existingUser.setDob(updatedUser.getDob() != null ? updatedUser.getDob() : existingUser.getDob());
                        existingUser.setGender(updatedUser.getGender() != null ? updatedUser.getGender() : existingUser.getGender());
                        existingUser.setAddress(updatedUser.getAddress() != null ? updatedUser.getAddress() : existingUser.getAddress());
                        existingUser.setRole(updatedUser.getRole() != null ? updatedUser.getRole() : existingUser.getRole());
                        existingUser.setStatus(updatedUser.getStatus() != null ? updatedUser.getStatus() : existingUser.getStatus());

                        userRepository.save(existingUser);
                        return new UserResponse<>(true, existingUser, null);
                    })
                    .orElse(new UserResponse<>(false, null, "User not found to update"));
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update userdetails: " + e.getMessage());
        }
    }

    public UserResponse<User> getUserByUsername(String username) {
        try {
            User user = userRepository.findByUsername(username.toLowerCase());
            if (user == null) {
                return new UserResponse<>(false, null, "User not found");
            }
            return new UserResponse<>(true, user, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to fetch userdetails: " + e.getMessage());
        }
    }

    public UserResponse<User> putUpdateProfile(String username, User updatedUser) {
        try {
            User existingUser = userRepository.findByUsername(username.toLowerCase());
            if (existingUser == null) {
                return new UserResponse<>(false, null, "User not found");
            }

            existingUser.setUsername(updatedUser.getUsername().toLowerCase());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setDob(updatedUser.getDob());
            existingUser.setGender(updatedUser.getGender());
            existingUser.setAddress(updatedUser.getAddress());

            userRepository.save(existingUser);
            return new UserResponse<>(true, existingUser, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update profile: " + e.getMessage());
        }
    }

    public UserResponse<User> patchUpdateProfile(String username, User updatedUser) {
        try {
            User existingUser = userRepository.findByUsername(username.toLowerCase());
            if (existingUser == null) {
                return new UserResponse<>(false, null, "User not found");
            }

            if (updatedUser.getUsername() != null)
                existingUser.setUsername(updatedUser.getUsername().toLowerCase());
            if (updatedUser.getEmail() != null)
                existingUser.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null)
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            if (updatedUser.getFirstName() != null)
                existingUser.setFirstName(updatedUser.getFirstName());
            if (updatedUser.getLastName() != null)
                existingUser.setLastName(updatedUser.getLastName());
            if (updatedUser.getPhone() != null)
                existingUser.setPhone(updatedUser.getPhone());
            if (updatedUser.getDob() != null)
                existingUser.setDob(updatedUser.getDob());
            if (updatedUser.getGender() != null)
                existingUser.setGender(updatedUser.getGender());
            if (updatedUser.getAddress() != null)
                existingUser.setAddress(updatedUser.getAddress());

            userRepository.save(existingUser);
            return new UserResponse<>(true, existingUser, null);
        } catch (Exception e) {
            return new UserResponse<>(false, null, "Failed to update profile: " + e.getMessage());
        }
    }

}
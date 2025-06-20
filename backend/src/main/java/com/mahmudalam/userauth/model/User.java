package com.mahmudalam.userauth.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username is mandatory")
    @Size(max = 50, message = "Username must be at most 50 characters")
    private String username;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    private String password;

    private String firstName;

    private String lastName;

    private String phone;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role is mandatory")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = Status.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = Status.ACTIVE;
        }
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum Role {
        USER, ADMIN
    }

    public enum Status{
        ACTIVE, INACTIVE, SUSPENDED
    }
}

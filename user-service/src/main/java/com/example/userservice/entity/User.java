package com.example.userservice.entity;


import com.example.userservice.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String firstName;

    private String middleName;

    @Column(nullable=false)
    private String lastName;

    @Column(nullable=false, unique=true)
    private String username;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String phoneNumber;

    private boolean enabled = true;

    private boolean accountNonLocked = true;

    private boolean emailVerified = false;

    @Column(nullable = false)
    private String provider;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


}

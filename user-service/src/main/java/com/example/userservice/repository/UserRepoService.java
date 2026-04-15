package com.example.userservice.repository;

import com.example.userservice.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepoService {

    void createUser(User user);


    Optional<User> findByUsername(String username);

    boolean existsByUsername(@NotBlank String username);

    boolean existsByEmail(@Email @NotBlank String email);


}

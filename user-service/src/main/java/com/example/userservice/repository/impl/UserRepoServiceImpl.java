package com.example.userservice.repository.impl;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepoService;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRepoServiceImpl  implements UserRepoService {

    private final UserRepository userRepository;
    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }


    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


}

package com.passwordmanager.password_manager.service;


import com.passwordmanager.password_manager.exceptions.UserNotFoundException;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(String userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No User found with id: " + userId));
    }
}

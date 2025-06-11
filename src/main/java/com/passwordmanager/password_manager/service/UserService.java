package com.passwordmanager.password_manager.service;


import com.passwordmanager.password_manager.dto.LoginDTO;
import com.passwordmanager.password_manager.exceptions.UserNotFoundException;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(String userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No User found with id: " + userId));
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("No User found with email: " + email));
    }

    public User registerNewUser(LoginDTO login) {
        //TODO: Do more checks (validation here)
        //Are more validations needed if we already implemented the validationhandler???

        
        //TODO: Hash the password here
        String hashedPassword = login.getPassword();

        User newUser = new User(login.getUsername(), login.getEmail(), hashedPassword);
        return userRepository.save(newUser);
    }
}

package com.passwordmanager.password_manager.service;


import com.passwordmanager.password_manager.controller.AuthController;
import com.passwordmanager.password_manager.dto.LoginRequestDTO;
import com.passwordmanager.password_manager.exceptions.UserNotFoundException;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.repository.UserRepository;
import com.passwordmanager.password_manager.security.EncryptionService;
import com.passwordmanager.password_manager.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    //Check: Service inside service??
    private final EncryptionService encryptionService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, EncryptionService encryptionService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public User getUserById(String userId) throws UserNotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("No User found with id: " + userId));
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("No User found with email: " + email));
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("No User found with email: " + username));
    }

    //TODO: Maybe custom exception? Should this method be here?
    public String login(LoginRequestDTO login) throws Exception {
        User user = userRepository.findByUsername(login.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(!encryptionService.matches(login.getPassword(), user.getMasterPasswordHash(), user.getSalt())) {
            log.error("Credentials do not match: " + user.getUsername());
            //TODO: CUstom exception here
            throw new Exception("");
        }
        log.info("User found");
        log.info("");

        return jwtService.generateToken(user);
    }

    public User registerNewUser(LoginRequestDTO login) throws Exception {
        log.info("Saving new User");
        //TODO: Do more checks (validation here)
        //Are more validations needed if we already implemented the validationhandler???
        String pass = login.getPassword();
        String userName = login.getUsername();
        String email = login.getEmail();
        String salt = encryptionService.generateNewSalt();

        //TODO: do validations here if needed?

        SecretKey newKey = encryptionService.deriveKey(pass, salt);
        String hashedPassword = encryptionService.encrypt(pass, newKey);

        //Create new user and save it
        User newUser = new User(userName, email, hashedPassword, salt);
        return userRepository.save(newUser);
    }
}

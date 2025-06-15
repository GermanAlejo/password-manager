package com.passwordmanager.password_manager.service;


import com.passwordmanager.password_manager.dto.LoginRequestDTO;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.exceptions.InvalidArgumentsException;
import com.passwordmanager.password_manager.exceptions.UserAlreadyRegisteredException;
import com.passwordmanager.password_manager.exceptions.UserNotFoundException;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.repository.UserRepository;
import com.passwordmanager.password_manager.security.EncryptionService;
import com.passwordmanager.password_manager.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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

    public boolean existsByUsernameOrEmail(String identifier) {
        return (userRepository.existsByEmail(identifier) || userRepository.existsByUsername(identifier));
    }

    public User findUserByUsernameOrEmail(String identifier) throws UsernameNotFoundException{
        return userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    //TODO: Maybe custom exception? Should this method be here?
    public String login(LoginRequestDTO loginRequestDTO) throws EncryptionException, UsernameNotFoundException {
        try {
            User user = findUserByUsernameOrEmail(loginRequestDTO.getLoginIdentifier());
            //Get decoded salt
            byte[] decodedSalt = encryptionService.decodeSalt(user.getSalt());
            if (!encryptionService.matches(loginRequestDTO.getPassword(), user.getMasterPasswordHash(), decodedSalt)) {
                log.error("Credentials do not match: " + user.getUsername());
                throw new InvalidArgumentsException("Username/Password not valid");
            }
            log.info("User found");
            log.info("");

            return jwtService.generateToken(user);
        } catch (BadPaddingException e) {
            throw new EncryptionException("Wrong key");
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Could not decrypt/encrypt", e);
        }
    }

    public User registerNewUser(LoginRequestDTO login) throws UserAlreadyRegisteredException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        log.info("Saving new User");
        //TODO: This validation enough?
        if(existsByUsernameOrEmail(login.getLoginIdentifier())) {
            log.error("User already in DB");
            throw new UserAlreadyRegisteredException("User already in DB");
        }

        //Are more validations needed if we already implemented the validationhandler???
        String pass = login.getPassword();
        String userName = login.getUsername();
        String email = login.getEmail();
        byte[] salt = encryptionService.generateSalt();

        String hashedPassword = encryptionService.hashPassword(pass, salt);
        //Encode the salt also
        String encodedSalt = encryptionService.encodeSalt(salt);
        //Create new user and save it
        User newUser = new User(userName, email, hashedPassword, encodedSalt);
        return userRepository.save(newUser);
    }
}

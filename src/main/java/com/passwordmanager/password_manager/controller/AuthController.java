package com.passwordmanager.password_manager.controller;

import com.passwordmanager.password_manager.config.JwtConfig;
import com.passwordmanager.password_manager.dto.AuthResponseDTO;
import com.passwordmanager.password_manager.dto.LoginRequestDTO;
import com.passwordmanager.password_manager.exceptions.UserNotFoundException;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }


    //TODO: Check DTO values here and call registration/login endpoint here

    @GetMapping("/login")
    public ResponseEntity<AuthResponseDTO> userLogin(@Valid @RequestBody LoginRequestDTO loginRequest) throws UserNotFoundException {
        // 1. Authenticate (throws exception if invalid)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        if(!authentication.isAuthenticated()) {
            throw new UserNotFoundException("Invalid Credentials");
        }
        // 2. Generate JWT
        //String token = jwtConfig.generateToken(authentication);

        // 3. Fetch user details
        //call service here
        User user = userService.getUserByUsername(loginRequest.getUsername());

        //TODO: Match the passwords here? or before?

        // 4. Return response
        //Maybe a token here?
        return ResponseEntity.ok(new AuthResponseDTO("", user.getUsername(), user.getEmail()));
    }

    //public ResponseEntity<User> registerNewUser()

}

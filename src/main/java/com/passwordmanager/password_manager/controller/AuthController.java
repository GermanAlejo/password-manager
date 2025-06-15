package com.passwordmanager.password_manager.controller;

import com.passwordmanager.password_manager.dto.AuthResponseDTO;
import com.passwordmanager.password_manager.dto.LoginResponseDTO;
import com.passwordmanager.password_manager.dto.LoginRequestDTO;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.model.User;
import com.passwordmanager.password_manager.security.EncryptionService;
import com.passwordmanager.password_manager.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService, EncryptionService encryptionService) {
        this.userService = userService;
    }


    //TODO: Check DTO values here and call registration/login endpoint here
    //TODO: Custom exception here
    @GetMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDTO loginRequest) throws EncryptionException {

        if(!loginRequest.isValid()) {
            return ResponseEntity.badRequest().body("Username or email must be provided");
        }

        log.info("Calling login service");
        String token = userService.login(loginRequest);
        log.info("");

        // 4. Return response
        //Maybe a token here?
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws Exception {
        log.info("Calling registration");
        if(!loginRequestDTO.isValid()) {
            return ResponseEntity.badRequest().body("Username or email must be provided");
        }
        User newUser = userService.registerNewUser(loginRequestDTO);
        log.info("");
        return ResponseEntity.ok(new AuthResponseDTO(newUser.getEmail(), newUser.getUsername()));
    }

}

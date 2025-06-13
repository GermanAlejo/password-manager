package com.passwordmanager.password_manager.controller;

import com.passwordmanager.password_manager.dto.AuthResponseDTO;
import com.passwordmanager.password_manager.dto.LoginRequestDTO;
import com.passwordmanager.password_manager.security.EncryptionService;
import com.passwordmanager.password_manager.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService, EncryptionService encryptionService) {
        this.userService = userService;
    }


    //TODO: Check DTO values here and call registration/login endpoint here

    @GetMapping("/login")
    public ResponseEntity<AuthResponseDTO> userLogin(@Valid @RequestBody LoginRequestDTO loginRequest) throws Exception {

        log.info("Calling login service");
        String token = userService.login(loginRequest);
        log.info("");

        // 4. Return response
        //Maybe a token here?
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    //public ResponseEntity<User> registerNewUser()

}

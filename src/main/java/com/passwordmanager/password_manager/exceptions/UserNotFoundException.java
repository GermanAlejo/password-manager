package com.passwordmanager.password_manager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotFoundException(String error) {
        super(error);
    }
}

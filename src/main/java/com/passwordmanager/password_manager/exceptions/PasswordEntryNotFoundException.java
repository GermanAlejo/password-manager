package com.passwordmanager.password_manager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Entry not found")
public class PasswordEntryNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public PasswordEntryNotFoundException(String error) {
        super(error);
    }

}

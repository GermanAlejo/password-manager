package com.passwordmanager.password_manager.exceptions;

import java.io.Serial;

public class UserAlreadyRegisteredException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}

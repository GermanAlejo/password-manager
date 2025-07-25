package com.passwordmanager.password_manager.exceptions;

import java.io.Serial;

public class InvalidArgumentsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidArgumentsException(String message) {
        super(message);
    }
}

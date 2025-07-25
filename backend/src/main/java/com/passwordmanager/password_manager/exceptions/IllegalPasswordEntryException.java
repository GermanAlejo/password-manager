package com.passwordmanager.password_manager.exceptions;

import java.io.Serial;

public class IllegalPasswordEntryException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public IllegalPasswordEntryException(String message) {
        super(message);
    }

}

package com.passwordmanager.password_manager.exceptions;


import java.io.Serial;

public class EncryptionException extends Exception {

  @Serial
  private static final long serialVersionUID = 1L;

  public EncryptionException(String message) {
    super(message);
  }

}

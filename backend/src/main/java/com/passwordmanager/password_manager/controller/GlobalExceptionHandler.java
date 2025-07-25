package com.passwordmanager.password_manager.controller;

import java.util.Map;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.passwordmanager.password_manager.exceptions.EncryptionException;
import com.passwordmanager.password_manager.exceptions.IllegalPasswordEntryException;
import com.passwordmanager.password_manager.exceptions.InvalidArgumentsException;
import com.passwordmanager.password_manager.exceptions.PasswordEntryNotFoundException;
import com.passwordmanager.password_manager.exceptions.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidArgumentsException.class)
  public ResponseEntity<?> handleInvalidArguments(InvalidArgumentsException ex) {
    return ResponseEntity
        .badRequest()
        .body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(IllegalPasswordEntryException.class)
  public  ResponseEntity<?> handleIllegalPasswordEntry(IllegalPasswordEntryException ex) {
    return ResponseEntity
        .badRequest()
        .body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(PasswordEntryNotFoundException.class)
  public ResponseEntity<?> handleEntryNotFound(PasswordEntryNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(EncryptionException.class)
  public ResponseEntity<?> handleEncryptionException(EncryptionException ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleGenericError(Exception ex) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", ex.getMessage()));
  }


}

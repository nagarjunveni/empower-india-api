package com.andhraempower.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(generateErrorMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ResponseEntity<>(generateErrorMessage(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>(generateErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(EntityNotFoundException ex) {
        return new ResponseEntity<>(generateErrorMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Map<String, String>> handleTokenExpiredException(TokenExpiredException ex) {
        return new ResponseEntity<>(generateErrorMessage(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RequiredFieldMissingException.class)
    public ResponseEntity<Map<String, String>> handleRequiredField(RequiredFieldMissingException ex) {
        return new ResponseEntity<>(generateErrorMessage(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingBearerTokenException.class)
    public ResponseEntity<Map<String, String>> handleMissingBearerTokenException(MissingBearerTokenException ex) {
        return new ResponseEntity<>(generateErrorMessage(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserInActiveException.class)
    public ResponseEntity<Map<String, String>> handleUserInActiveException(UserInActiveException ex) {
        return new ResponseEntity<>(generateErrorMessage(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        return new ResponseEntity<>(generateErrorMessage(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Map<String, String> generateErrorMessage(String message) {
        return Map.of("errorMessage",message);
    }

}

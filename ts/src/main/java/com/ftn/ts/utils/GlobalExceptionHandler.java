package com.ftn.ts.utils;

import com.ftn.ts.exceptions.NotActivatedException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.ftn.ts.exceptions.ActivationExpiredException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        LOGGER.error("❌ EntityNotFound caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        LOGGER.error("❌ UsernameNotFound caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + ex.getMessage());
    }
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        LOGGER.error("❌ AuthorizationDenied caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization error: " + ex.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        LOGGER.error("❌ IllegalArgument caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        LOGGER.error("❌ RuntimeException caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Runtime error: " + ex.getMessage());
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        LOGGER.error("❌ IOException caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handleMessagingException(MessagingException ex) {
        LOGGER.error("❌ MessagingException caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
    @ExceptionHandler(ActivationExpiredException.class)
    public ResponseEntity<String> handleActivationExpiredException(ActivationExpiredException ex) {
        LOGGER.error("❌ ActivationExpiredException caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.GONE).body("Activation link expired, please register again. " + ex.getMessage());
    }
    @ExceptionHandler(NotActivatedException.class)
    public ResponseEntity<String> handleNotActivatedException(NotActivatedException ex) {
        LOGGER.error("❌ NotActivatedException caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is not activated, please check your email: " + ex.getMessage());
    }
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<String> handleFileUploadException(FileUploadException ex) {
        LOGGER.error("❌ FileUploadException caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error uploading files! " + ex.getMessage());
    }
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException ex) {
        LOGGER.error("❌ FileNotFoundException caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        LOGGER.error("❌ MethodArgumentNotValidException caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request argument: " + ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        LOGGER.error("❌ Exception caught: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + ex.getMessage() + "    " + ex.getClass());
    }
}

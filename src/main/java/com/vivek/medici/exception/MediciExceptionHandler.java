package com.vivek.medici.exception;

import com.vivek.medici.controller.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MediciExceptionHandler {

    Logger logger = LogManager.getLogger(MediciExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        Map<String, Object> result  = new HashMap<>();
        result.put("errors", errors);
        logger.error("MethodArgumentNotValidException " , result);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        Map<String, Object> result  = new HashMap<>();
        result.put("errors", ex.getMessage());
        logger.error("UserAlreadyExistsException " , result);
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(InvalidCredentialException ex) {
        Map<String, String> result  = new HashMap<>();
        result.put("message", ex.getMessage());
        logger.error("InvalidCredentialException " , result);
        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(RuntimeException ex) {
        Map<String, String> result  = new HashMap<>();
        result.put("message", ex.getMessage());
        logger.error("RuntimeException " , result);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}

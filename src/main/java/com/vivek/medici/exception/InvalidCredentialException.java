package com.vivek.medici.exception;

public class InvalidCredentialException extends RuntimeException {
    public InvalidCredentialException() {}
    public InvalidCredentialException(String message) {
        super(message);
    }
}

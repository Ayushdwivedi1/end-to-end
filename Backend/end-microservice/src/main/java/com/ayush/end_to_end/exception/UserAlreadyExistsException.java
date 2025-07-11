package com.ayush.end_to_end.exception;

public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String email) {
        super("User already exists with email: " + email);
    }
} 
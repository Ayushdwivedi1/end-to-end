package com.ayush.end_to_end.service;

public interface EmailService {
    
    void sendOtpEmail(String to, String otp);
    
    void sendPasswordResetEmail(String to, String resetLink);
} 
package service;

import com.ayush.end_to_end.dto.AuthResponse;
import com.ayush.end_to_end.dto.ChangePasswordRequest;
import com.ayush.end_to_end.dto.ForgetPasswordRequest;
import com.ayush.end_to_end.dto.LoginRequest;
import com.ayush.end_to_end.dto.RegisterRequest;
import com.ayush.end_to_end.dto.VerifyOtpRequest;

public interface AuthService {
    
    AuthResponse register(RegisterRequest registerRequest);
    
    AuthResponse login(LoginRequest loginRequest);
    
    String logout(String usernameOrEmail);
    
    String forgetPassword(ForgetPasswordRequest forgetPasswordRequest);
    
    String verifyOtpAndResetPassword(VerifyOtpRequest verifyOtpRequest);
    
    String changePassword(ChangePasswordRequest changePasswordRequest);
} 
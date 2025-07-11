package rest.impl;

import com.ayush.end_to_end.dto.ApiResponse;
import com.ayush.end_to_end.dto.AuthResponse;
import com.ayush.end_to_end.dto.ChangePasswordRequest;
import com.ayush.end_to_end.dto.ForgetPasswordRequest;
import com.ayush.end_to_end.dto.LoginRequest;
import com.ayush.end_to_end.dto.RegisterRequest;
import com.ayush.end_to_end.dto.VerifyOtpRequest;
import com.ayush.end_to_end.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import service.AuthService;
import authRest.AuthRest;

@Service
public class AuthRestImpl implements AuthRest {
    
    private static final Logger log = LoggerFactory.getLogger(AuthRestImpl.class);
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public ResponseEntity<ApiResponse<AuthResponse>> register(RegisterRequest registerRequest) {
        log.info("Registration request received for username: {}", registerRequest.getUsername());
        AuthResponse authResponse = authService.register(registerRequest);
        log.info("User registered successfully with ID: {}", authResponse.getId());
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", authResponse));
    }
    
    @Override
    public ResponseEntity<ApiResponse<AuthResponse>> login(LoginRequest loginRequest) {
        log.info("Login request received for: {}", loginRequest.getUsernameOrEmail());
        AuthResponse authResponse = authService.login(loginRequest);
        log.info("User logged in successfully with ID: {}", authResponse.getId());
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> logout(String usernameOrEmail) {
        log.info("Logout request received for: {}", usernameOrEmail);
        String result = authService.logout(usernameOrEmail);
        log.info("User logged out successfully: {}", usernameOrEmail);
        return ResponseEntity.ok(ApiResponse.success("Logout successful", result));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> forgetPassword(ForgetPasswordRequest forgetPasswordRequest) {
        log.info("Forget password request received for email: {}", forgetPasswordRequest.getEmail());
        String result = authService.forgetPassword(forgetPasswordRequest);
        log.info("Forget password request processed successfully for email: {}", forgetPasswordRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.success("OTP sent to your email", result));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> verifyOtpAndResetPassword(VerifyOtpRequest verifyOtpRequest) {
        log.info("OTP verification request received for email: {}", verifyOtpRequest.getEmail());
        String result = authService.verifyOtpAndResetPassword(verifyOtpRequest);
        log.info("Password reset successfully for email: {}", verifyOtpRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully", result));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> changePassword(ChangePasswordRequest changePasswordRequest) {
        log.info("Change password request received for: {}", changePasswordRequest.getUsernameOrEmail());
        String result = authService.changePassword(changePasswordRequest);
        log.info("Password changed successfully for: {}", changePasswordRequest.getUsernameOrEmail());
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", result));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> testEmail(String email) {
        log.info("Test email request received for: {}", email);
        try {
            emailService.sendOtpEmail(email, "123456");
            log.info("Test email sent successfully to: {}", email);
            return ResponseEntity.ok(ApiResponse.success("Test email sent successfully"));
        } catch (Exception e) {
            log.error("Test email failed for: {} - Error: {}", email, e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("Test email failed: " + e.getMessage()));
        }
    }
} 
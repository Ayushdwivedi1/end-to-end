package authRest;

import com.ayush.end_to_end.dto.ApiResponse;
import com.ayush.end_to_end.dto.AuthResponse;
import com.ayush.end_to_end.dto.ChangePasswordRequest;
import com.ayush.end_to_end.dto.ForgetPasswordRequest;
import com.ayush.end_to_end.dto.LoginRequest;
import com.ayush.end_to_end.dto.RegisterRequest;
import com.ayush.end_to_end.dto.VerifyOtpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public interface AuthRest {
    
    @PostMapping("/register")
    ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest registerRequest);
    
    @PostMapping("/login")
    ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest loginRequest);
    
    @PostMapping("/logout")
    ResponseEntity<ApiResponse<String>> logout(@RequestParam String usernameOrEmail);
    
    @PostMapping("/forget-password")
    ResponseEntity<ApiResponse<String>> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest);
    
    @PostMapping("/verify-otp")
    ResponseEntity<ApiResponse<String>> verifyOtpAndResetPassword(@RequestBody VerifyOtpRequest verifyOtpRequest);
    
    @PostMapping("/change-password")
    ResponseEntity<ApiResponse<String>> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest);
    
    @PostMapping("/test-email")
    ResponseEntity<ApiResponse<String>> testEmail(@RequestParam String email);
} 
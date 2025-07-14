import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  LoginRequest, 
  RegisterRequest, 
  AuthResponse, 
  ForgetPasswordRequest, 
  VerifyOtpRequest, 
  ChangePasswordRequest, 
  ApiResponse 
} from '../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  // Register user
  register(registerRequest: RegisterRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.apiUrl}/register`, registerRequest);
  }

  // Login user
  login(loginRequest: LoginRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http.post<ApiResponse<AuthResponse>>(`${this.apiUrl}/login`, loginRequest);
  }

  // Logout user
  logout(usernameOrEmail: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.apiUrl}/logout?usernameOrEmail=${usernameOrEmail}`, {});
  }

  // Forget password
  forgetPassword(forgetPasswordRequest: ForgetPasswordRequest): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.apiUrl}/forget-password`, forgetPasswordRequest);
  }

  // Verify OTP and reset password
  verifyOtpAndResetPassword(verifyOtpRequest: VerifyOtpRequest): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.apiUrl}/verify-otp`, verifyOtpRequest);
  }

  // Change password
  changePassword(changePasswordRequest: ChangePasswordRequest): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.apiUrl}/change-password`, changePasswordRequest);
  }

  // Test email
  testEmail(email: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(`${this.apiUrl}/test-email?email=${email}`, {});
  }

  // Store user data in localStorage
  setUserData(userData: AuthResponse): void {
    localStorage.setItem('userData', JSON.stringify(userData));
  }

  // Get user data from localStorage
  getUserData(): AuthResponse | null {
    const userData = localStorage.getItem('userData');
    return userData ? JSON.parse(userData) : null;
  }

  // Clear user data from localStorage
  clearUserData(): void {
    localStorage.removeItem('userData');
  }

  // Check if user is logged in
  isLoggedIn(): boolean {
    return this.getUserData() !== null;
  }

  // Store redirect URL
  setRedirectUrl(url: string): void {
    localStorage.setItem('redirectUrl', url);
  }

  // Get redirect URL
  getRedirectUrl(): string | null {
    return localStorage.getItem('redirectUrl');
  }

  // Clear redirect URL
  clearRedirectUrl(): void {
    localStorage.removeItem('redirectUrl');
  }
} 
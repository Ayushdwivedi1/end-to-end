import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  changePasswordForm: FormGroup;
  verifyOtpForm: FormGroup;
  currentStep: 'email' | 'otp' | 'password' = 'email';
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  email = '';
  loggedInUserEmail = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.changePasswordForm = this.fb.group({
      email: ['']
    });

    this.verifyOtpForm = this.fb.group({
      otp: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {
    // Get the logged-in user's email
    const userData = this.authService.getUserData();
    if (userData && userData.email) {
      this.loggedInUserEmail = userData.email;
      this.email = userData.email;
      // Pre-fill the email field
      this.changePasswordForm.patchValue({
        email: userData.email
      });
    } else {
      // If no user data, redirect to login
      this.router.navigate(['/login']);
    }
  }

  passwordMatchValidator(form: FormGroup) {
    const newPassword = form.get('newPassword')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { passwordMismatch: true };
  }

  onSendOtp() {
    if (this.email && this.email.trim() !== '') {
      this.isLoading = true;
      this.errorMessage = '';

      this.authService.forgetPassword({ email: this.email }).subscribe({
        next: (response) => {
          this.isLoading = false;
          if (response.success) {
            this.successMessage = 'OTP sent successfully to your email!';
            this.currentStep = 'otp';
            setTimeout(() => {
              this.successMessage = '';
            }, 3000);
          } else {
            this.errorMessage = response.message || 'Failed to send OTP';
          }
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = error.error?.message || 'Failed to send OTP. Please try again.';
        }
      });
    }
  }

  onVerifyOtp() {
    if (this.verifyOtpForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      const verifyOtpRequest = {
        email: this.email,
        otp: this.verifyOtpForm.get('otp')?.value,
        newPassword: this.verifyOtpForm.get('newPassword')?.value,
        confirmPassword: this.verifyOtpForm.get('confirmPassword')?.value
      };

      this.authService.verifyOtpAndResetPassword(verifyOtpRequest).subscribe({
        next: (response) => {
          this.isLoading = false;
          if (response.success) {
            this.successMessage = 'Password changed successfully! Redirecting to dashboard...';
            setTimeout(() => {
              this.router.navigate(['/dashboard']);
            }, 2000);
          } else {
            this.errorMessage = response.message || 'Failed to change password';
          }
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = error.error?.message || 'Failed to change password. Please try again.';
        }
      });
    }
  }

  goBack() {
    if (this.currentStep === 'otp') {
      this.currentStep = 'email';
      this.verifyOtpForm.reset();
    }
  }

  goToUsers() {
    this.router.navigate(['/dashboard']);
  }

  resendOtp() {
    this.onSendOtp();
  }
} 
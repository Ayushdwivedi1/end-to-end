import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.css']
})
export class ForgetPasswordComponent implements OnInit {
  forgetPasswordForm: FormGroup;
  otpForm: FormGroup;
  newPasswordForm: FormGroup;
  currentStep: number = 1;
  loading = false;
  errorMessage = '';
  successMessage = '';
  infoMessage = '';
  email = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.forgetPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });

    this.otpForm = this.fb.group({
      otp: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });

    this.newPasswordForm = this.fb.group({
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    // If user is logged in, clear their session and redirect to login
    if (this.authService.isLoggedIn()) {
      this.authService.clearUserData();
      this.router.navigate(['/login'], { 
        queryParams: { 
          message: 'Please log out first to reset your password. You can only reset passwords for emails you can access.' 
        } 
      });
      return;
    }
    
    // Ensure email field is always empty
    this.forgetPasswordForm.patchValue({ email: '' });
  }

  passwordMatchValidator(form: FormGroup) {
    const newPassword = form.get('newPassword')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { passwordMismatch: true };
  }

  onSubmit() {
    if (this.forgetPasswordForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      this.successMessage = '';
      this.infoMessage = '';
      this.email = this.forgetPasswordForm.get('email')?.value;

      this.authService.forgetPassword({ email: this.email }).subscribe({
        next: (response) => {
          this.loading = false;
          if (response.success) {
            this.successMessage = 'OTP sent successfully to your email!';
            this.currentStep = 2;
            setTimeout(() => {
              this.successMessage = '';
            }, 3000);
          } else {
            this.errorMessage = response.message || 'Failed to send OTP';
          }
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage = error.error?.message || 'Failed to send OTP. Please try again.';
        }
      });
    }
  }

  verifyOtp() {
    if (this.otpForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      this.successMessage = '';
      this.infoMessage = '';

      const otp = this.otpForm.get('otp')?.value;

      // For now, we'll just move to the next step
      // In a real implementation, you would verify the OTP here
      this.currentStep = 3;
      this.loading = false;
      this.infoMessage = 'OTP verified successfully! Please enter your new password.';
    }
  }

  resetPassword() {
    if (this.newPasswordForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      this.successMessage = '';
      this.infoMessage = '';

      const verifyOtpRequest = {
        email: this.email,
        otp: this.otpForm.get('otp')?.value,
        newPassword: this.newPasswordForm.get('newPassword')?.value,
        confirmPassword: this.newPasswordForm.get('confirmPassword')?.value
      };

      this.authService.verifyOtpAndResetPassword(verifyOtpRequest).subscribe({
        next: (response) => {
          this.loading = false;
          if (response.success) {
            this.successMessage = 'Password reset successfully! Redirecting to login...';
            setTimeout(() => {
              this.router.navigate(['/login']);
            }, 2000);
          } else {
            this.errorMessage = response.message || 'Failed to reset password';
          }
        },
        error: (error) => {
          this.loading = false;
          this.errorMessage = error.error?.message || 'Failed to reset password. Please try again.';
        }
      });
    }
  }

  getErrorMessage(fieldName: string): string {
    const field = this.forgetPasswordForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) {
        return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
      }
      if (field.errors['email']) {
        return 'Please enter a valid email address';
      }
    }
    return '';
  }

  getOtpErrorMessage(fieldName: string): string {
    const field = this.otpForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) {
        return 'OTP is required';
      }
      if (field.errors['minlength'] || field.errors['maxlength']) {
        return 'OTP must be 6 digits';
      }
    }
    return '';
  }

  getNewPasswordErrorMessage(fieldName: string): string {
    const field = this.newPasswordForm.get(fieldName);
    if (field?.errors) {
      if (field.errors['required']) {
        return `${fieldName === 'newPassword' ? 'New password' : 'Confirm password'} is required`;
      }
      if (field.errors['minlength']) {
        return 'Password must be at least 6 characters';
      }
    }
    return '';
  }

  goBack() {
    if (this.currentStep === 2) {
      this.currentStep = 1;
      this.otpForm.reset();
    } else if (this.currentStep === 3) {
      this.currentStep = 2;
      this.newPasswordForm.reset();
    }
  }

  resendOtp() {
    this.onSubmit();
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
} 
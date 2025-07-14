import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  errorMessage = '';
  successMessage = '';
  infoMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.loginForm = this.fb.group({
      usernameOrEmail: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
    // Check for query parameters (e.g., when redirected from forget password)
    this.route.queryParams.subscribe(params => {
      if (params['message']) {
        this.infoMessage = params['message'];
        // Clear the message after 5 seconds
        setTimeout(() => {
          this.infoMessage = '';
        }, 5000);
      }
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      this.successMessage = '';

      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          if (response.success) {
            this.authService.setUserData(response.data!);
            this.successMessage = 'Login successful!';
            setTimeout(() => {
              // Check if there's a redirect URL
              const redirectUrl = this.authService.getRedirectUrl();
              if (redirectUrl && redirectUrl !== '/login') {
                this.authService.clearRedirectUrl();
                this.router.navigate([redirectUrl]);
              } else {
                this.router.navigate(['/dashboard']);
              }
            }, 1000);
          } else {
            this.errorMessage = response.message;
          }
          this.loading = false;
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Login failed. Please try again.';
          this.loading = false;
        }
      });
    }
  }

  getErrorMessage(field: string): string {
    const control = this.loginForm.get(field);
    if (control?.hasError('required')) {
      return `${field.charAt(0).toUpperCase() + field.slice(1)} is required`;
    }
    if (control?.hasError('minlength')) {
      return `${field.charAt(0).toUpperCase() + field.slice(1)} must be at least 6 characters`;
    }
    return '';
  }
} 
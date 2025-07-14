import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  user: User | null = null;
  loading = false;
  errorMessage = '';
  isFromAuthData = false;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    console.log('ProfileComponent initialized');
    this.loadUserProfile();
  }

  loadUserProfile(): void {
    const userData = this.authService.getUserData();
    if (!userData) {
      this.errorMessage = 'User data not found. Please login again.';
      return;
    }

    console.log('Loading profile for user:', userData);
    this.loading = true;
    
    // Try to get user by ID first, then by email as fallback
    if (userData.id) {
      console.log('Loading profile by ID:', userData.id);
      this.userService.getUserById(userData.id).subscribe({
        next: (response) => {
          console.log('Profile data received by ID:', response);
          if (response.success && response.data) {
            this.user = response.data;
          } else {
            // If not found by ID, try by email
            this.loadUserByEmail(userData.email);
          }
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading profile by ID:', error);
          // If error with ID, try by email
          this.loadUserByEmail(userData.email);
        }
      });
    } else if (userData.email) {
      // Fallback to email if no ID
      this.loadUserByEmail(userData.email);
    } else {
      this.errorMessage = 'User data incomplete. Please login again.';
      this.loading = false;
    }
  }

  private loadUserByEmail(email: string): void {
    console.log('Loading profile by email:', email);
    this.userService.getUserByEmail(email).subscribe({
      next: (response) => {
        console.log('Profile data received by email:', response);
        if (response.success && response.data) {
          this.user = response.data;
        } else {
          // If user not found in user table, create a user object from auth data
          this.createUserFromAuthData();
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading profile by email:', error);
        // If user not found in user table, create a user object from auth data
        this.createUserFromAuthData();
        this.loading = false;
      }
    });
  }

  private createUserFromAuthData(): void {
    const userData = this.authService.getUserData();
    if (userData) {
      // Create a user object from auth data
      this.user = {
        id: userData.id,
        email: userData.email,
        firstName: userData.firstName || '',
        lastName: userData.lastName || '',
        phoneNumber: '',
        address: '',
        isActive: true,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      };
      this.isFromAuthData = true;
      console.log('Created user from auth data:', this.user);
    } else {
      this.errorMessage = 'Unable to load profile data. Please login again.';
    }
  }

  onEdit(): void {
    if (this.user?.id) {
      this.router.navigate(['/users', this.user.id, 'edit']);
    }
  }

  onBack(): void {
    this.router.navigate(['/dashboard']);
  }

  logout(): void {
    const userData = this.authService.getUserData();
    
    // Clear user data immediately to prevent any auth guard interference
    this.authService.clearUserData();
    
    if (userData?.username) {
      // Navigate to login immediately, then handle the logout API call
      this.router.navigate(['/login']);
      
      // Make the logout API call in the background
      this.authService.logout(userData.username).subscribe({
        next: () => {
          console.log('Logout successful');
        },
        error: (error) => {
          console.error('Logout API error:', error);
          // Even if the API call fails, user is already logged out locally
        }
      });
    } else {
      // If no user data, just navigate to login
      this.router.navigate(['/login']);
    }
  }
} 
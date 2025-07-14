import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'AI Central';
  isScrolled = false;
  userData: any = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.checkScroll();
    this.loadUserData();
  }

  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    this.checkScroll();
  }

  private checkScroll(): void {
    this.isScrolled = window.scrollY > 50;
  }

  private loadUserData(): void {
    this.userData = this.authService.getUserData();
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  isOnLoginOrRegisterPage(): boolean {
    return this.router.url === '/login' || this.router.url === '/register' || this.router.url === '/';
  }

  isOnChatPage(): boolean {
    return this.router.url === '/chat';
  }

  logout(): void {
    const userData = this.authService.getUserData();
    
    // Clear user data immediately to prevent any auth guard interference
    this.authService.clearUserData();
    
    if (userData) {
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
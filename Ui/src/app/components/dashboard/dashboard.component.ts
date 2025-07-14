import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  userData: any;
  crudOperations = [
    {
      title: 'AI User Analytics',
      description: 'Intelligent user behavior analysis and insights',
      icon: 'bi bi-graph-up-arrow',
      route: '/users',
      color: 'primary',
      action: 'Analyze'
    },
    {
      title: 'Smart User Creation',
      description: 'AI-assisted user registration with validation',
      icon: 'bi bi-robot',
      route: '/users/create',
      color: 'success',
      action: 'Create'
    },
    {
      title: 'Intelligent Management',
      description: 'AI-powered user management and optimization',
      icon: 'bi bi-cpu',
      route: '/users',
      color: 'info',
      action: 'Manage'
    },
    {
      title: 'AI Profile Assistant',
      description: 'Smart profile management with AI recommendations',
      icon: 'bi bi-person-gear',
      route: '/profile',
      color: 'warning',
      action: 'Profile'
    },
    {
      title: 'Neural Security',
      description: 'Advanced AI-powered security and authentication',
      icon: 'bi bi-shield-check',
      route: '/change-password',
      color: 'secondary',
      action: 'Secure'
    },
    {
      title: 'AI System Monitor',
      description: 'Real-time AI system performance and metrics',
      icon: 'bi bi-speedometer2',
      route: '/users',
      color: 'dark',
      action: 'Monitor'
    }
  ];

  aiStats = [
    {
      title: 'AI Processing',
      value: '99.9%',
      description: 'System Efficiency',
      icon: 'bi bi-lightning-charge-fill',
      color: '#78dbff'
    },
    {
      title: 'Smart Users',
      value: 'Active',
      description: 'Real-time Status',
      icon: 'bi bi-people-fill',
      color: '#4ade80'
    },
    {
      title: 'Neural Network',
      value: 'Online',
      description: 'AI Core Status',
      icon: 'bi bi-cpu-fill',
      color: '#fbbf24'
    },
    {
      title: 'Security AI',
      value: 'Protected',
      description: 'Threat Detection',
      icon: 'bi bi-shield-fill-check',
      color: '#a78bfa'
    }
  ];

  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit() {
    this.userData = this.authService.getUserData();
    if (!this.userData) {
      this.router.navigate(['/login']);
    }
  }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }

  logout() {
    if (this.userData) {
      // Clear user data immediately to prevent any auth guard interference
      this.authService.clearUserData();
      
      // Navigate to login immediately, then handle the logout API call
      this.router.navigate(['/login']);
      
      // Make the logout API call in the background
      this.authService.logout(this.userData.username).subscribe({
        next: () => {
          console.log('Logout successful');
        },
        error: (error) => {
          console.error('Logout API error:', error);
          // Even if the API call fails, user is already logged out locally
        }
      });
    }
  }
}

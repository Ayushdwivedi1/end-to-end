import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-view',
  templateUrl: './user-view.component.html',
  styleUrls: ['./user-view.component.css']
})
export class UserViewComponent implements OnInit {
  user: User | null = null;
  userId: number | null = null;
  loading = false;
  errorMessage = '';

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    console.log('UserViewComponent initialized');
    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      console.log('User ID from params:', this.userId);
      if (this.userId) {
        this.loadUser();
      }
    });
  }

  loadUser(): void {
    if (!this.userId) return;

    console.log('Loading user with ID:', this.userId);
    this.loading = true;
    this.userService.getUserById(this.userId).subscribe({
      next: (response) => {
        console.log('User data received:', response);
        if (response.success && response.data) {
          this.user = response.data;
        } else {
          this.errorMessage = response.message || 'Failed to load user';
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading user:', error);
        this.errorMessage = error.error?.message || 'Failed to load user';
        this.loading = false;
      }
    });
  }

  onEdit(): void {
    if (this.userId) {
      this.router.navigate(['/users', this.userId, 'edit']);
    }
  }

  onBack(): void {
    this.router.navigate(['/users']);
  }
} 
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  loading = false;
  errorMessage = '';
  searchTerm = '';
  showActiveOnly = false;

  constructor(
    private userService: UserService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading = true;
    this.errorMessage = '';

    const observable = this.showActiveOnly 
      ? this.userService.getActiveUsers()
      : this.userService.getAllUsers();

    observable.subscribe({
      next: (response) => {
        if (response.success) {
          this.users = response.data || [];
        } else {
          this.errorMessage = response.message;
        }
        this.loading = false;
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to load users';
        this.loading = false;
      }
    });
  }

  searchUsers(): void {
    if (this.searchTerm.trim()) {
      this.loading = true;
      this.errorMessage = '';

      this.userService.searchUsersByName(this.searchTerm).subscribe({
        next: (response) => {
          if (response.success) {
            this.users = response.data || [];
          } else {
            this.errorMessage = response.message;
          }
          this.loading = false;
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Failed to search users';
          this.loading = false;
        }
      });
    } else {
      this.loadUsers();
    }
  }

  toggleActiveFilter(): void {
    this.showActiveOnly = !this.showActiveOnly;
    this.loadUsers();
  }

  deleteUser(userId: number): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUser(userId).subscribe({
        next: (response) => {
          if (response.success) {
            this.users = this.users.filter(user => user.id !== userId);
          } else {
            this.errorMessage = response.message;
          }
        },
        error: (error) => {
          this.errorMessage = error.error?.message || 'Failed to delete user';
        }
      });
    }
  }

  deactivateUser(userId: number): void {
    this.userService.deactivateUser(userId).subscribe({
      next: (response) => {
        if (response.success) {
          this.loadUsers(); // Reload to get updated status
        } else {
          this.errorMessage = response.message;
        }
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to deactivate user';
      }
    });
  }

  activateUser(userId: number): void {
    this.userService.activateUser(userId).subscribe({
      next: (response) => {
        if (response.success) {
          this.loadUsers(); // Reload to get updated status
        } else {
          this.errorMessage = response.message;
        }
      },
      error: (error) => {
        this.errorMessage = error.error?.message || 'Failed to activate user';
      }
    });
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.loadUsers();
  }

  viewUser(userId: number): void {
    // Navigate to user view page
    console.log('Navigating to user view for ID:', userId);
    const url = `/users/${userId}`;
    console.log('Navigation URL:', url);
    this.router.navigate(['/users', userId]).then(() => {
      console.log('Navigation completed successfully');
    }).catch(err => {
      console.error('Navigation error:', err);
    });
  }

  editUser(userId: number): void {
    // Navigate to user edit page
    this.router.navigate(['/users', userId, 'edit']);
  }
} 
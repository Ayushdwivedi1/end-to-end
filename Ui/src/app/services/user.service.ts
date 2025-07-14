import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User, UserUpdate, ApiResponse } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  // Get all users
  getAllUsers(): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/users`);
  }

  // Get active users
  getActiveUsers(): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/users/active`);
  }

  // Get user by ID
  getUserById(id: number): Observable<ApiResponse<User>> {
    console.log('UserService: Making GET request to:', `${this.apiUrl}/users/${id}`);
    return this.http.get<ApiResponse<User>>(`${this.apiUrl}/users/${id}`);
  }

  // Get user by email
  getUserByEmail(email: string): Observable<ApiResponse<User>> {
    return this.http.get<ApiResponse<User>>(`${this.apiUrl}/users/email/${email}`);
  }

  // Search users by name
  searchUsersByName(name: string): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(`${this.apiUrl}/users/search?name=${name}`);
  }

  // Create user
  createUser(user: User): Observable<ApiResponse<User>> {
    return this.http.post<ApiResponse<User>>(`${this.apiUrl}/users`, user);
  }

  // Update user
  updateUser(id: number, userUpdate: UserUpdate): Observable<ApiResponse<User>> {
    return this.http.put<ApiResponse<User>>(`${this.apiUrl}/users/${id}`, userUpdate);
  }

  // Delete user
  deleteUser(id: number): Observable<ApiResponse<string>> {
    return this.http.delete<ApiResponse<string>>(`${this.apiUrl}/users/${id}`);
  }

  // Deactivate user
  deactivateUser(id: number): Observable<ApiResponse<string>> {
    return this.http.patch<ApiResponse<string>>(`${this.apiUrl}/users/${id}/deactivate`, {});
  }

  // Activate user
  activateUser(id: number): Observable<ApiResponse<string>> {
    return this.http.patch<ApiResponse<string>>(`${this.apiUrl}/users/${id}/activate`, {});
  }

  // Test endpoint
  test(): Observable<string> {
    return this.http.get(`${this.apiUrl}/test`, { responseType: 'text' });
  }
} 
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface AiChatRequest {
  question: string;
  userId: string;
  conversationId?: string;
}

export interface AiChatResponse {
  response: string;
  confidenceScore: number;
  languageUsed: string;
  sessionId: string | null;
  conversationId: number;
  learningNotes: string | null;
  isLearned: boolean;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export interface AiFeedbackRequest {
  conversationId: string;
  questionId: string;
  rating: number;
  feedback: string;
}

export interface AiConversation {
  id: string;
  userId: string;
  title: string;
  createdAt: string;
  updatedAt: string;
  messageCount: number;
}

@Injectable({
  providedIn: 'root'
})
export class AiChatService {
  private apiUrl = environment.apiUrl + '/api/ai-chat';

  constructor(private http: HttpClient) {}

  // Send a question to the AI
  sendQuestion(request: AiChatRequest): Observable<AiChatResponse> {
    return this.http.post<ApiResponse<AiChatResponse>>(`${this.apiUrl}/ask`, request)
      .pipe(
        map(response => response.data)
      );
  }

  // Get conversation history
  getConversations(userId: string): Observable<AiConversation[]> {
    return this.http.get<ApiResponse<AiConversation[]>>(`${this.apiUrl}/conversations/${userId}`)
      .pipe(
        map(response => response.data)
      );
  }

  // Get messages from a specific conversation
  getConversationMessages(conversationId: string): Observable<AiChatResponse[]> {
    return this.http.get<ApiResponse<AiChatResponse[]>>(`${this.apiUrl}/conversation/${conversationId}/messages`)
      .pipe(
        map(response => response.data)
      );
  }

  // Submit feedback for an AI response
  submitFeedback(feedback: AiFeedbackRequest): Observable<any> {
    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/feedback`, feedback)
      .pipe(
        map(response => response.data)
      );
  }

  // Get AI performance statistics
  getPerformanceStats(userId: string): Observable<any> {
    return this.http.get<ApiResponse<any>>(`${this.apiUrl}/stats/${userId}`)
      .pipe(
        map(response => response.data)
      );
  }

  // Delete a conversation
  deleteConversation(conversationId: string): Observable<any> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/conversation/${conversationId}`)
      .pipe(
        map(response => response.data)
      );
  }

  // Clear all conversations for a user
  clearAllConversations(userId: string): Observable<any> {
    return this.http.delete<ApiResponse<any>>(`${this.apiUrl}/conversations/${userId}`)
      .pipe(
        map(response => response.data)
      );
  }
} 
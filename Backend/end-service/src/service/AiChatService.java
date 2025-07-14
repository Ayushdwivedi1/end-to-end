package service;

import com.ayush.end_to_end.dto.AiChatRequest;
import com.ayush.end_to_end.dto.AiChatResponse;
import com.ayush.end_to_end.dto.AiFeedbackRequest;
import com.ayush.end_to_end.dto.ApiResponse;

import java.util.List;

public interface AiChatService {
    
    /**
     * Process user question and generate AI response
     */
    ApiResponse<AiChatResponse> processQuestion(AiChatRequest request);
    
    /**
     * Provide feedback on AI response for learning
     */
    ApiResponse<String> provideFeedback(AiFeedbackRequest request);
    
    /**
     * Get conversation history for a user
     */
    ApiResponse<List<AiChatResponse>> getConversationHistory(Long userId, String sessionId);
    
    /**
     * Get learning insights and statistics
     */
    ApiResponse<Object> getLearningInsights();
    
    /**
     * Get similar questions for context
     */
    ApiResponse<List<String>> getSimilarQuestions(String keyword);
    
    /**
     * Learn from feedback and improve responses
     */
    ApiResponse<String> learnFromFeedback();
    
    /**
     * Get AI performance statistics
     */
    ApiResponse<Object> getPerformanceStats();
    
    /**
     * Clear conversation history for a session
     */
    ApiResponse<String> clearSessionHistory(String sessionId);
    
    /**
     * Get conversation by ID
     */
    ApiResponse<AiChatResponse> getConversationById(Long conversationId);
    
    /**
     * Update conversation learning status
     */
    ApiResponse<String> updateLearningStatus(Long conversationId, Boolean isLearned, String learningNotes);
} 
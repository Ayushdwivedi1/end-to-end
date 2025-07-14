

import com.ayush.end_to_end.dto.AiChatRequest;
import com.ayush.end_to_end.dto.AiChatResponse;
import com.ayush.end_to_end.dto.AiFeedbackRequest;
import com.ayush.end_to_end.dto.ApiResponse;

import java.util.List;

public interface AiChatRest {
    
    /**
     * Process user question and get AI response
     */
    ApiResponse<AiChatResponse> processQuestion(AiChatRequest request);
    
    /**
     * Provide feedback on AI response
     */
    ApiResponse<String> provideFeedback(AiFeedbackRequest request);
    
    /**
     * Get conversation history
     */
    ApiResponse<List<AiChatResponse>> getConversationHistory(Long userId, String sessionId);
    
    /**
     * Get learning insights
     */
    ApiResponse<Object> getLearningInsights();
    
    /**
     * Get similar questions
     */
    ApiResponse<List<String>> getSimilarQuestions(String keyword);
    
    /**
     * Trigger learning from feedback
     */
    ApiResponse<String> learnFromFeedback();
    
    /**
     * Get performance statistics
     */
    ApiResponse<Object> getPerformanceStats();
    
    /**
     * Clear session history
     */
    ApiResponse<String> clearSessionHistory(String sessionId);
    
    /**
     * Get conversation by ID
     */
    ApiResponse<AiChatResponse> getConversationById(Long conversationId);
    
    /**
     * Update learning status
     */
    ApiResponse<String> updateLearningStatus(Long conversationId, Boolean isLearned, String learningNotes);
} 
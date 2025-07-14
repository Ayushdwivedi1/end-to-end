package com.ayush.end_to_end.repository;

import com.ayush.end_to_end.entity.AiConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiConversationRepository extends JpaRepository<AiConversation, Long> {
    
    // Find conversations by user
    List<AiConversation> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    // Find conversations by session
    List<AiConversation> findBySessionIdOrderByCreatedAtDesc(String sessionId);
    
    // Find conversations by language
    List<AiConversation> findByLanguageUsedOrderByCreatedAtDesc(String languageUsed);
    
    // Find learned conversations
    List<AiConversation> findByIsLearnedTrueOrderByCreatedAtDesc();
    
    // Find conversations by feedback
    List<AiConversation> findByUserFeedbackOrderByCreatedAtDesc(String feedback);
    
    // Find recent conversations for learning
    @Query("SELECT ac FROM AiConversation ac WHERE ac.isLearned = false ORDER BY ac.createdAt DESC")
    List<AiConversation> findRecentUnlearnedConversations();
    
    // Find conversations with low confidence for improvement
    @Query("SELECT ac FROM AiConversation ac WHERE ac.confidenceScore < :threshold ORDER BY ac.confidenceScore ASC")
    List<AiConversation> findLowConfidenceConversations(@Param("threshold") Double threshold);
    
    // Find similar questions for context
    @Query("SELECT ac FROM AiConversation ac WHERE LOWER(ac.userQuestion) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY ac.createdAt DESC")
    List<AiConversation> findSimilarQuestions(@Param("keyword") String keyword);
    
    // Count conversations by user
    Long countByUserId(Long userId);
    
    // Count learned conversations
    Long countByIsLearnedTrue();
    
    // Find conversations for specific user and session
    List<AiConversation> findByUserIdAndSessionIdOrderByCreatedAtDesc(Long userId, String sessionId);
    
    // Find conversations with specific feedback for learning
    @Query("SELECT ac FROM AiConversation ac WHERE ac.userFeedback = :feedback AND ac.isLearned = false ORDER BY ac.createdAt DESC")
    List<AiConversation> findUnlearnedConversationsByFeedback(@Param("feedback") String feedback);
    
    // Count conversations by feedback
    Long countByUserFeedback(String feedback);
} 
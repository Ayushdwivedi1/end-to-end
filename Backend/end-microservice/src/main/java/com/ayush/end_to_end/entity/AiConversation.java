package com.ayush.end_to_end.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_conversations")
public class AiConversation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_question", columnDefinition = "TEXT")
    private String userQuestion;
    
    @Column(name = "ai_response", columnDefinition = "TEXT")
    private String aiResponse;
    
    @Column(name = "user_feedback")
    private String userFeedback; // good, bad, neutral
    
    @Column(name = "confidence_score")
    private Double confidenceScore;
    
    @Column(name = "language_used")
    private String languageUsed; // hindi, english, mixed
    
    @Column(name = "conversation_context", columnDefinition = "TEXT")
    private String conversationContext;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @Column(name = "is_learned")
    private Boolean isLearned = false;
    
    @Column(name = "learning_notes", columnDefinition = "TEXT")
    private String learningNotes;
    
    // Constructors
    public AiConversation() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public AiConversation(String userQuestion, String aiResponse, User user) {
        this();
        this.userQuestion = userQuestion;
        this.aiResponse = aiResponse;
        this.user = user;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUserQuestion() {
        return userQuestion;
    }
    
    public void setUserQuestion(String userQuestion) {
        this.userQuestion = userQuestion;
    }
    
    public String getAiResponse() {
        return aiResponse;
    }
    
    public void setAiResponse(String aiResponse) {
        this.aiResponse = aiResponse;
    }
    
    public String getUserFeedback() {
        return userFeedback;
    }
    
    public void setUserFeedback(String userFeedback) {
        this.userFeedback = userFeedback;
    }
    
    public Double getConfidenceScore() {
        return confidenceScore;
    }
    
    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }
    
    public String getLanguageUsed() {
        return languageUsed;
    }
    
    public void setLanguageUsed(String languageUsed) {
        this.languageUsed = languageUsed;
    }
    
    public String getConversationContext() {
        return conversationContext;
    }
    
    public void setConversationContext(String conversationContext) {
        this.conversationContext = conversationContext;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public Boolean getIsLearned() {
        return isLearned;
    }
    
    public void setIsLearned(Boolean isLearned) {
        this.isLearned = isLearned;
    }
    
    public String getLearningNotes() {
        return learningNotes;
    }
    
    public void setLearningNotes(String learningNotes) {
        this.learningNotes = learningNotes;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
} 
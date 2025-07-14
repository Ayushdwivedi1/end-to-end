package com.ayush.end_to_end.dto;

public class AiChatResponse {
    private String response;
    private Double confidenceScore;
    private String languageUsed;
    private String sessionId;
    private Long conversationId;
    private String learningNotes;
    private Boolean isLearned;
    
    // Constructors
    public AiChatResponse() {}
    
    public AiChatResponse(String response, String sessionId) {
        this.response = response;
        this.sessionId = sessionId;
    }
    
    // Getters and Setters
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
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
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public Long getConversationId() {
        return conversationId;
    }
    
    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
    
    public String getLearningNotes() {
        return learningNotes;
    }
    
    public void setLearningNotes(String learningNotes) {
        this.learningNotes = learningNotes;
    }
    
    public Boolean getIsLearned() {
        return isLearned;
    }
    
    public void setIsLearned(Boolean isLearned) {
        this.isLearned = isLearned;
    }
} 
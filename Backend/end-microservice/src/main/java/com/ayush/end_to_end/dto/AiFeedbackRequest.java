package com.ayush.end_to_end.dto;

public class AiFeedbackRequest {
    private Long conversationId;
    private String feedback; // good, bad, neutral
    private String userComment;
    private String userId;
    
    // Constructors
    public AiFeedbackRequest() {}
    
    public AiFeedbackRequest(Long conversationId, String feedback) {
        this.conversationId = conversationId;
        this.feedback = feedback;
    }
    
    // Getters and Setters
    public Long getConversationId() {
        return conversationId;
    }
    
    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    public String getUserComment() {
        return userComment;
    }
    
    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
} 
package com.ayush.end_to_end.dto;

public class AiChatRequest {
    private String question;
    private String sessionId;
    private String languagePreference; // hindi, english, mixed
    private String context;
    private String userId;
    
    // Constructors
    public AiChatRequest() {}
    
    public AiChatRequest(String question, String sessionId) {
        this.question = question;
        this.sessionId = sessionId;
    }
    
    // Getters and Setters
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getLanguagePreference() {
        return languagePreference;
    }
    
    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }
    
    public String getContext() {
        return context;
    }
    
    public void setContext(String context) {
        this.context = context;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
} 
package rest.impl;

import com.ayush.end_to_end.dto.AiChatRequest;
import com.ayush.end_to_end.dto.AiChatResponse;
import com.ayush.end_to_end.dto.AiFeedbackRequest;
import com.ayush.end_to_end.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.AiChatService;

import java.util.List;

@RestController
@RequestMapping("/api/ai-chat")
@CrossOrigin(origins = "*")
public class AiChatRestImpl {
    
    @Autowired
    private AiChatService aiChatService;
    
    @PostMapping("/ask")
    public ApiResponse<AiChatResponse> processQuestion(@RequestBody AiChatRequest request) {
        return aiChatService.processQuestion(request);
    }
    
    @PostMapping("/feedback")
    public ApiResponse<String> provideFeedback(@RequestBody AiFeedbackRequest request) {
        return aiChatService.provideFeedback(request);
    }
    
    @GetMapping("/history")
    public ApiResponse<List<AiChatResponse>> getConversationHistory(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String sessionId) {
        return aiChatService.getConversationHistory(userId, sessionId);
    }
    
    @GetMapping("/insights")
    public ApiResponse<Object> getLearningInsights() {
        return aiChatService.getLearningInsights();
    }
    
    @GetMapping("/similar-questions")
    public ApiResponse<List<String>> getSimilarQuestions(@RequestParam String keyword) {
        return aiChatService.getSimilarQuestions(keyword);
    }
    
    @PostMapping("/learn")
    public ApiResponse<String> learnFromFeedback() {
        return aiChatService.learnFromFeedback();
    }
    
    @GetMapping("/stats")
    public ApiResponse<Object> getPerformanceStats() {
        return aiChatService.getPerformanceStats();
    }
    
    @DeleteMapping("/clear-session")
    public ApiResponse<String> clearSessionHistory(@RequestParam String sessionId) {
        return aiChatService.clearSessionHistory(sessionId);
    }
    
    @GetMapping("/conversation/{id}")
    public ApiResponse<AiChatResponse> getConversationById(@PathVariable Long id) {
        return aiChatService.getConversationById(id);
    }
    
    @PutMapping("/learning-status/{id}")
    public ApiResponse<String> updateLearningStatus(
            @PathVariable Long id,
            @RequestParam Boolean isLearned,
            @RequestParam(required = false) String learningNotes) {
        return aiChatService.updateLearningStatus(id, isLearned, learningNotes);
    }
} 
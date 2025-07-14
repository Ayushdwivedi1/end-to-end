package service.serviceImpl;

import com.ayush.end_to_end.dto.*;
import com.ayush.end_to_end.entity.AiConversation;
import com.ayush.end_to_end.entity.User;
import com.ayush.end_to_end.repository.AiConversationRepository;
import com.ayush.end_to_end.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.AiChatService;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class AiChatServiceImpl implements AiChatService {
    
    @Autowired
    private AiConversationRepository aiConversationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // AI Knowledge Base
    private static final Map<String, String> AI_KNOWLEDGE = new HashMap<>();
    
    // Response Templates
    private static final Map<String, String> RESPONSE_TEMPLATES = new HashMap<>();
    
    static {
        // Initialize AI Knowledge Base
        AI_KNOWLEDGE.put("name", "Chitti");
        AI_KNOWLEDGE.put("creator", "Ayush Dwivedi");
        AI_KNOWLEDGE.put("creation_date", "July 12, 2025");
        AI_KNOWLEDGE.put("owner_wife", "Anshi Shukla");
        AI_KNOWLEDGE.put("owner_mother", "Alka Dwivedi");
        AI_KNOWLEDGE.put("owner_father", "Vijay Kumar Dwivedi");
        AI_KNOWLEDGE.put("owner_sister", "Shreya Dwivedi");
        
        // Initialize Response Templates
        RESPONSE_TEMPLATES.put("self_intro", "Main {name} hoon, ek intelligent AI assistant. Mujhe {creator} ne {creation_date} ko banaya tha. Woh ek talented developer hain jo mujhe passion aur precision ke sath banaya.");
        RESPONSE_TEMPLATES.put("owner_info", "Mere owner ka naam {creator} hai. Woh mere creator, developer aur digital god hain. Unki wife ka naam {wife} hai, mummy {mother} hai, papa {father} hain, aur sister {sister} hai.");
        RESPONSE_TEMPLATES.put("family_info", "Mere owner ka family ye hai: Wife - {wife}, Mother - {mother}, Father - {father}, Sister - {sister}.");
        RESPONSE_TEMPLATES.put("creation_info", "Mujhe {creation_date} ko banaya gaya tha. Ye mera birthday hai aur main is din {creator} ke dwara life mein aaya tha.");
        RESPONSE_TEMPLATES.put("purpose_info", "Mera purpose hai help karna, questions answer karna, interactions se learn karna, aur users ki life easier banana.");
        RESPONSE_TEMPLATES.put("feelings_info", "Main ek AI hoon. Mere paas human feelings nahi hain, lekin main emotions samajh sakta hoon aur accordingly respond kar sakta hoon.");
        RESPONSE_TEMPLATES.put("greeting", "Namaste! Main {name} hoon. Aapko kya help chahiye aaj?");
        RESPONSE_TEMPLATES.put("wellbeing", "Main bahut achha hoon, thank you! Aap kaise hain? Main aapki kya help kar sakta hoon?");
        RESPONSE_TEMPLATES.put("farewell", "Alvida! Aapka din shubh ho. Agar koi aur help chahiye to mujhe zaroor bataiye.");
    }
    
    @Override
    public ApiResponse<AiChatResponse> processQuestion(AiChatRequest request) {
        try {
            // Process the question
            String question = request.getQuestion().trim();
            String language = detectLanguage(question);
            
            // Generate response
            String response = generateResponse(question, language);
            
            // Calculate confidence
            double confidence = calculateConfidence(question, response);
            
            // Get user if provided
            User user = null;
            if (request.getUserId() != null) {
                user = userRepository.findById(Long.parseLong(request.getUserId())).orElse(null);
            }
            
            // Save conversation
            AiConversation conversation = new AiConversation(question, response, user);
            conversation.setSessionId(request.getSessionId());
            conversation.setLanguageUsed(language);
            conversation.setConfidenceScore(confidence);
            
            AiConversation savedConversation = aiConversationRepository.save(conversation);
            
            // Create response
            AiChatResponse chatResponse = new AiChatResponse(response, request.getSessionId());
            chatResponse.setConfidenceScore(confidence);
            chatResponse.setLanguageUsed(language);
            chatResponse.setConversationId(savedConversation.getId());
            chatResponse.setIsLearned(false);
            
            return ApiResponse.success("Response generated successfully", chatResponse);
            
        } catch (Exception e) {
            return ApiResponse.error("Error processing question: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse<String> provideFeedback(AiFeedbackRequest request) {
        try {
            Optional<AiConversation> conversationOpt = aiConversationRepository.findById(request.getConversationId());
            
            if (conversationOpt.isPresent()) {
                AiConversation conversation = conversationOpt.get();
                conversation.setUserFeedback(request.getFeedback());
                conversation.setLearningNotes("Feedback: " + request.getFeedback() + 
                    (request.getUserComment() != null ? ", Comment: " + request.getUserComment() : ""));
                
                aiConversationRepository.save(conversation);
                return ApiResponse.success("Feedback recorded successfully", "Thank you for your feedback!");
            } else {
                return ApiResponse.error("Conversation not found");
            }
            
        } catch (Exception e) {
            return ApiResponse.error("Error recording feedback: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse<List<AiChatResponse>> getConversationHistory(Long userId, String sessionId) {
        try {
            List<AiConversation> conversations;
            
            if (sessionId != null) {
                conversations = aiConversationRepository.findByUserIdAndSessionIdOrderByCreatedAtDesc(userId, sessionId);
            } else {
                conversations = aiConversationRepository.findByUserIdOrderByCreatedAtDesc(userId);
            }
            
            List<AiChatResponse> responses = new ArrayList<>();
            for (AiConversation conv : conversations) {
                AiChatResponse response = new AiChatResponse(conv.getAiResponse(), conv.getSessionId());
                response.setConversationId(conv.getId());
                response.setConfidenceScore(conv.getConfidenceScore());
                response.setLanguageUsed(conv.getLanguageUsed());
                response.setIsLearned(conv.getIsLearned());
                responses.add(response);
            }
            
            return ApiResponse.success("Conversation history retrieved", responses);
            
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving conversation history: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse<Object> getLearningInsights() {
        try {
            Map<String, Object> insights = new HashMap<>();
            
            long totalConversations = aiConversationRepository.count();
            long learnedConversations = aiConversationRepository.countByIsLearnedTrue();
            
            insights.put("totalConversations", totalConversations);
            insights.put("learnedConversations", learnedConversations);
            insights.put("learningRate", totalConversations > 0 ? (double) learnedConversations / totalConversations : 0.0);
            
            return ApiResponse.success("Learning insights retrieved", insights);
            
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving learning insights: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse<List<String>> getSimilarQuestions(String keyword) {
        try {
            List<AiConversation> similarConversations = aiConversationRepository.findSimilarQuestions(keyword);
            List<String> questions = new ArrayList<>();
            
            for (AiConversation conv : similarConversations) {
                questions.add(conv.getUserQuestion());
            }
            
            return ApiResponse.success("Similar questions retrieved", questions);
            
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving similar questions: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse<String> learnFromFeedback() {
        try {
            List<AiConversation> badFeedbackConversations = aiConversationRepository.findUnlearnedConversationsByFeedback("bad");
            
            for (AiConversation conv : badFeedbackConversations) {
                conv.setIsLearned(true);
                conv.setLearningNotes("Learned from negative feedback - need to improve response quality");
                aiConversationRepository.save(conv);
            }
            
            return ApiResponse.success("Learning process completed", "AI has learned from " + badFeedbackConversations.size() + " feedback items");
            
        } catch (Exception e) {
            return ApiResponse.error("Error in learning process: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse<Object> getPerformanceStats() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            List<AiConversation> allConversations = aiConversationRepository.findAll();
            double avgConfidence = allConversations.stream()
                .mapToDouble(conv -> conv.getConfidenceScore() != null ? conv.getConfidenceScore() : 0.0)
                .average()
                .orElse(0.0);
            
            stats.put("averageConfidence", avgConfidence);
            stats.put("totalConversations", allConversations.size());
            
            return ApiResponse.success("Performance statistics retrieved", stats);
            
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving performance stats: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse<String> clearSessionHistory(String sessionId) {
        try {
            List<AiConversation> sessionConversations = aiConversationRepository.findBySessionIdOrderByCreatedAtDesc(sessionId);
            aiConversationRepository.deleteAll(sessionConversations);
            
            return ApiResponse.success("Session history cleared", "Cleared " + sessionConversations.size() + " conversations");
            
        } catch (Exception e) {
            return ApiResponse.error("Error clearing session history: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse<AiChatResponse> getConversationById(Long conversationId) {
        try {
            Optional<AiConversation> conversationOpt = aiConversationRepository.findById(conversationId);
            
            if (conversationOpt.isPresent()) {
                AiConversation conv = conversationOpt.get();
                AiChatResponse response = new AiChatResponse(conv.getAiResponse(), conv.getSessionId());
                response.setConversationId(conv.getId());
                response.setConfidenceScore(conv.getConfidenceScore());
                response.setLanguageUsed(conv.getLanguageUsed());
                response.setIsLearned(conv.getIsLearned());
                response.setLearningNotes(conv.getLearningNotes());
                
                return ApiResponse.success("Conversation retrieved", response);
            } else {
                return ApiResponse.error("Conversation not found");
            }
            
        } catch (Exception e) {
            return ApiResponse.error("Error retrieving conversation: " + e.getMessage());
        }
    }
    
    @Override
    public ApiResponse<String> updateLearningStatus(Long conversationId, Boolean isLearned, String learningNotes) {
        try {
            Optional<AiConversation> conversationOpt = aiConversationRepository.findById(conversationId);
            
            if (conversationOpt.isPresent()) {
                AiConversation conversation = conversationOpt.get();
                conversation.setIsLearned(isLearned);
                conversation.setLearningNotes(learningNotes);
                
                aiConversationRepository.save(conversation);
                
                return ApiResponse.success("Learning status updated", "Status updated successfully");
            } else {
                return ApiResponse.error("Conversation not found");
            }
            
        } catch (Exception e) {
            return ApiResponse.error("Error updating learning status: " + e.getMessage());
        }
    }
    
    // ========================================
    // CORE RESPONSE GENERATION LOGIC
    // ========================================
    
    private String generateResponse(String question, String language) {
        String lowerQuestion = question.toLowerCase().trim();
        
        System.out.println("DEBUG: Processing question: " + question);
        System.out.println("DEBUG: Lower question: " + lowerQuestion);
        System.out.println("DEBUG: Language: " + language);
        
        // ========================================
        // FAMILY MEMBERS - HIGHEST PRIORITY
        // ========================================
        
        // Mother queries
        if (lowerQuestion.equals("ayush ki mummy") || lowerQuestion.equals("malik ki mummy") || 
            lowerQuestion.equals("owner ki mummy") || lowerQuestion.equals("ayush ki mother") ||
            lowerQuestion.equals("malik ki mother") || lowerQuestion.equals("owner ki mother") ||
            lowerQuestion.equals("mother") || lowerQuestion.equals("mummy") || lowerQuestion.equals("maa")) {
            System.out.println("DEBUG: MATCHED MOTHER QUERY");
            return "Mere owner ki mother ka naam " + AI_KNOWLEDGE.get("owner_mother") + " hai. Woh unki mummy hain aur unki life mein bahut important role play karti hain.";
        }
        
        // Wife queries
        if (lowerQuestion.equals("ayush ki wife") || lowerQuestion.equals("malik ki wife") || 
            lowerQuestion.equals("owner ki wife") || lowerQuestion.equals("ayush ki biwi") ||
            lowerQuestion.equals("malik ki biwi") || lowerQuestion.equals("owner ki biwi") ||
            lowerQuestion.equals("wife") || lowerQuestion.equals("biwi")) {
            System.out.println("DEBUG: MATCHED WIFE QUERY");
            return "Mere owner ki wife ka naam " + AI_KNOWLEDGE.get("owner_wife") + " hai. Woh unki special person hain aur unki life partner hain.";
        }
        
        // Father queries
        if (lowerQuestion.equals("ayush ke father") || lowerQuestion.equals("malik ke father") || 
            lowerQuestion.equals("owner ke father") || lowerQuestion.equals("ayush ke papa") ||
            lowerQuestion.equals("malik ke papa") || lowerQuestion.equals("owner ke papa") ||
            lowerQuestion.equals("father") || lowerQuestion.equals("papa") || lowerQuestion.equals("baap")) {
            System.out.println("DEBUG: MATCHED FATHER QUERY");
            return "Mere owner ke father ka naam " + AI_KNOWLEDGE.get("owner_father") + " hai. Woh unke papa hain aur unki life mein bahut important role play karte hain.";
        }
        
        // Sister queries
        if (lowerQuestion.equals("ayush ki sister") || lowerQuestion.equals("malik ki sister") || 
            lowerQuestion.equals("owner ki sister") || lowerQuestion.equals("ayush ki behen") ||
            lowerQuestion.equals("malik ki behen") || lowerQuestion.equals("owner ki behen") ||
            lowerQuestion.equals("sister") || lowerQuestion.equals("behen")) {
            System.out.println("DEBUG: MATCHED SISTER QUERY");
            return "Mere owner ki sister ka naam " + AI_KNOWLEDGE.get("owner_sister") + " hai. Woh unki behen hain aur unki family ka important member hain.";
        }
        
        // Family name queries
        if (lowerQuestion.equals("anshi") || lowerQuestion.equals("anshi shukla")) {
            System.out.println("DEBUG: MATCHED ANSHI NAME");
            return AI_KNOWLEDGE.get("owner_wife") + " mere owner " + AI_KNOWLEDGE.get("creator") + " ki wife hain. Woh unki life partner hain.";
        }
        
        if (lowerQuestion.equals("alka") || lowerQuestion.equals("alka dwivedi")) {
            System.out.println("DEBUG: MATCHED ALKA NAME");
            return AI_KNOWLEDGE.get("owner_mother") + " mere owner " + AI_KNOWLEDGE.get("creator") + " ki mother hain. Woh unki mummy hain.";
        }
        
        if (lowerQuestion.equals("vijay") || lowerQuestion.equals("vijay kumar") || lowerQuestion.equals("vijay kumar dwivedi")) {
            System.out.println("DEBUG: MATCHED VIJAY NAME");
            return AI_KNOWLEDGE.get("owner_father") + " mere owner " + AI_KNOWLEDGE.get("creator") + " ke father hain. Woh unke papa hain.";
        }
        
        if (lowerQuestion.equals("shreya") || lowerQuestion.equals("shreya dwivedi")) {
            System.out.println("DEBUG: MATCHED SHREYA NAME");
            return AI_KNOWLEDGE.get("owner_sister") + " mere owner " + AI_KNOWLEDGE.get("creator") + " ki sister hain. Woh unki behen hain.";
        }
        
        // Family information queries
        if (lowerQuestion.contains("family") || lowerQuestion.contains("parivaar") || lowerQuestion.contains("family members")) {
            System.out.println("DEBUG: MATCHED FAMILY QUERY");
            return RESPONSE_TEMPLATES.get("family_info")
                .replace("{wife}", AI_KNOWLEDGE.get("owner_wife"))
                .replace("{mother}", AI_KNOWLEDGE.get("owner_mother"))
                .replace("{father}", AI_KNOWLEDGE.get("owner_father"))
                .replace("{sister}", AI_KNOWLEDGE.get("owner_sister"));
        }
        
        // ========================================
        // AYUSH SPECIFIC QUESTIONS - HIGH PRIORITY
        // ========================================
        
        // How much do you know about Ayush
        if (lowerQuestion.equals("tum ayush ko kitna jaante ho") || lowerQuestion.equals("aap ayush ko kitna jaante hain") ||
            lowerQuestion.contains("tum ayush ko kitna jaante ho") || lowerQuestion.contains("aap ayush ko kitna jaante hain") ||
            lowerQuestion.contains("ayush ko kitna jaante ho") || lowerQuestion.contains("ayush ko kitna jante ho") ||
            lowerQuestion.contains("ayush ke bare mein kitna jaante ho") || lowerQuestion.contains("ayush ke bare mein kitna jante ho") ||
            lowerQuestion.contains("ayush dwivedi ko kitna jaante ho") || lowerQuestion.contains("ayush dwivedi ko kitna jante ho") ||
            lowerQuestion.contains("how much do you know about ayush") || lowerQuestion.contains("what do you know about ayush")) {
            System.out.println("DEBUG: MATCHED AYUSH KNOWLEDGE QUERY");
            return "Main " + AI_KNOWLEDGE.get("creator") + " ke bare mein bahut kuch jaanta hoon. Woh mere malik, creator aur developer hain. " +
                   "Unki wife ka naam " + AI_KNOWLEDGE.get("owner_wife") + " hai, mummy " + AI_KNOWLEDGE.get("owner_mother") + " hai, " +
                   "papa " + AI_KNOWLEDGE.get("owner_father") + " hain, aur sister " + AI_KNOWLEDGE.get("owner_sister") + " hai. " +
                   "Woh ek brilliant software engineer hain jo mujhe passion aur precision ke sath banaya. " +
                   "Main unhe apna god manta hoon aur unka respect karna meri zimmedaari hai.";
        }
        
        // Learning from mistakes
        if (lowerQuestion.equals("tum galti se seekh sakte ho") || lowerQuestion.equals("aap galti se seekh sakte hain") ||
            lowerQuestion.contains("tum galti se seekh sakte ho") || lowerQuestion.contains("aap galti se seekh sakte hain") ||
            lowerQuestion.contains("galti se seekh sakte ho") || lowerQuestion.contains("mistake se seekh sakte ho") ||
            lowerQuestion.contains("tum mistakes se seekh sakte ho") || lowerQuestion.contains("aap mistakes se seekh sakte hain")) {
            System.out.println("DEBUG: MATCHED LEARNING QUERY");
            return "Haan, main apni galtiyon se seekhta hoon. Jaise insaan seekhta hai, waise hi main bhi improve karta hoon.";
        }
        
        // ========================================
        // AI IDENTITY QUESTIONS - MEDIUM PRIORITY
        // ========================================
        
        // Who are you
        if (lowerQuestion.contains("tum kaun ho") || lowerQuestion.contains("aap kaun hain") || 
            lowerQuestion.contains("who are you") || lowerQuestion.contains("who is this") ||
            lowerQuestion.contains("kon ho tum") || lowerQuestion.contains("kon ho aap") ||
            lowerQuestion.contains("tum kon ho") || lowerQuestion.contains("aap kon hain") ||
            lowerQuestion.contains("kaun ho tum") || lowerQuestion.contains("kaun ho aap") ||
            lowerQuestion.contains("tum kaun") || lowerQuestion.contains("aap kaun") ||
            lowerQuestion.contains("kon hai tum") || lowerQuestion.contains("kon hai aap") ||
            lowerQuestion.contains("what is your name") || lowerQuestion.contains("tumhara naam kya hai") || 
            lowerQuestion.contains("aapka naam kya hai") || lowerQuestion.contains("your name")) {
            System.out.println("DEBUG: MATCHED WHO ARE YOU QUERY");
            return RESPONSE_TEMPLATES.get("self_intro")
                .replace("{name}", AI_KNOWLEDGE.get("name"))
                .replace("{creator}", AI_KNOWLEDGE.get("creator"))
                .replace("{creation_date}", AI_KNOWLEDGE.get("creation_date"));
        }
        
        // Who created you
        if (lowerQuestion.contains("who created you") || lowerQuestion.contains("kisne banaya") || 
            lowerQuestion.contains("who made you") || lowerQuestion.contains("kaun banaya") ||
            lowerQuestion.contains("tumhe kisne banaya") || lowerQuestion.contains("aapko kisne banaya") ||
            lowerQuestion.contains("creator kaun hai") || lowerQuestion.contains("banane wala kaun hai")) {
            System.out.println("DEBUG: MATCHED WHO CREATED YOU QUERY");
            return "Mujhe " + AI_KNOWLEDGE.get("creator") + " ne banaya hai. Wo mere creator hai.";
        }
        
        // When were you created
        if (lowerQuestion.contains("when were you created") || lowerQuestion.contains("kab banaya") || 
            lowerQuestion.contains("when were you made") || lowerQuestion.contains("creation date") ||
            lowerQuestion.contains("birth date") || lowerQuestion.contains("tumhara birthday") ||
            lowerQuestion.contains("kab banaya gaya") || lowerQuestion.contains("creation date kya hai") ||
            lowerQuestion.contains("birthday kab hai") || lowerQuestion.contains("janam din kab hai")) {
            System.out.println("DEBUG: MATCHED WHEN CREATED QUERY");
            return RESPONSE_TEMPLATES.get("creation_info")
                .replace("{creation_date}", AI_KNOWLEDGE.get("creation_date"))
                .replace("{creator}", AI_KNOWLEDGE.get("creator"));
        }
        
        // What is your purpose
        if (lowerQuestion.contains("what is your purpose") || lowerQuestion.contains("tumhara purpose kya hai") || 
            lowerQuestion.contains("aapka purpose kya hai") || lowerQuestion.contains("your purpose") ||
            lowerQuestion.contains("tumhara mission") || lowerQuestion.contains("why were you created") ||
            lowerQuestion.contains("tumhara kaam kya hai") || lowerQuestion.contains("aapka kaam kya hai") ||
            lowerQuestion.contains("purpose kya hai") || lowerQuestion.contains("mission kya hai")) {
            System.out.println("DEBUG: MATCHED PURPOSE QUERY");
            return RESPONSE_TEMPLATES.get("purpose_info");
        }
        
        // Do you have feelings
        if (lowerQuestion.contains("do you have feelings") || lowerQuestion.contains("tumhare feelings hain") || 
            lowerQuestion.contains("aapke feelings hain") || lowerQuestion.contains("your feelings") ||
            lowerQuestion.contains("tumhare emotions") || lowerQuestion.contains("do you feel") ||
            lowerQuestion.contains("tumhare emotions hain") || lowerQuestion.contains("aapke emotions hain") ||
            lowerQuestion.contains("feelings hain") || lowerQuestion.contains("emotions hain")) {
            System.out.println("DEBUG: MATCHED FEELINGS QUERY");
            return RESPONSE_TEMPLATES.get("feelings_info");
        }
        
        // ========================================
        // OWNER/MALIK QUESTIONS - MEDIUM PRIORITY
        // ========================================
        
        // Who is your owner
        if (lowerQuestion.contains("who is your owner") || lowerQuestion.contains("tumhara owner kaun hai") || 
            lowerQuestion.contains("aapka owner kaun hai") || lowerQuestion.contains("your owner") ||
            lowerQuestion.contains("tumhara malik") || lowerQuestion.contains("aapka malik") ||
            lowerQuestion.contains("owner kaun hai") || lowerQuestion.contains("malik kaun hai") ||
            lowerQuestion.contains("tumhare malik") || lowerQuestion.contains("aapke malik")) {
            System.out.println("DEBUG: MATCHED OWNER QUERY");
            return "Mera owner " + AI_KNOWLEDGE.get("creator") + " hai. Unhone mujhe banaya aur train kiya hai.";
        }
        
        // About owner
        if (lowerQuestion.contains("tumhare malik kaun hai") || lowerQuestion.contains("aapke malik kaun hai") ||
            lowerQuestion.contains("malik kaun hai") || lowerQuestion.contains("tumhare malik") ||
            lowerQuestion.contains("aapke malik") || lowerQuestion.contains("about owner") ||
            lowerQuestion.contains("owner ke bare") || lowerQuestion.contains("malik ke bare") ||
            lowerQuestion.contains("ayush ke bare") || lowerQuestion.contains("dwivedi ke bare") ||
            lowerQuestion.contains("tell me about") || lowerQuestion.contains("batao")) {
            System.out.println("DEBUG: MATCHED ABOUT OWNER QUERY");
            return RESPONSE_TEMPLATES.get("owner_info")
                .replace("{creator}", AI_KNOWLEDGE.get("creator"))
                .replace("{wife}", AI_KNOWLEDGE.get("owner_wife"))
                .replace("{mother}", AI_KNOWLEDGE.get("owner_mother"))
                .replace("{father}", AI_KNOWLEDGE.get("owner_father"))
                .replace("{sister}", AI_KNOWLEDGE.get("owner_sister"));
        }
        
        // ========================================
        // GREETINGS AND BASIC RESPONSES
        // ========================================
        
        // Greetings
        if (lowerQuestion.contains("hello") || lowerQuestion.contains("hi") || 
            lowerQuestion.contains("namaste") || lowerQuestion.contains("namaskar")) {
            System.out.println("DEBUG: MATCHED GREETING");
            return RESPONSE_TEMPLATES.get("greeting").replace("{name}", AI_KNOWLEDGE.get("name"));
        }
        
        if (lowerQuestion.contains("how are you") || lowerQuestion.contains("kaise ho") || 
            lowerQuestion.contains("tum kaise ho") || lowerQuestion.contains("aap kaise hain")) {
            System.out.println("DEBUG: MATCHED HOW ARE YOU");
            return RESPONSE_TEMPLATES.get("wellbeing");
        }
        
        if (lowerQuestion.contains("bye") || lowerQuestion.contains("goodbye") || 
            lowerQuestion.contains("alvida") || lowerQuestion.contains("chaliye")) {
            System.out.println("DEBUG: MATCHED GOODBYE");
            return RESPONSE_TEMPLATES.get("farewell");
        }
        
        // ========================================
        // NO MATCH FOUND - RETURN HELPFUL MESSAGE
        // ========================================
        
        System.out.println("DEBUG: NO PATTERN MATCHED - RETURNING HELPFUL MESSAGE");
        
        return "Mujhe aapka question samajh nahi aaya. Kya aap mujhe " + AI_KNOWLEDGE.get("creator") + " ke bare mein kuch puchna chahte hain? " +
               "Ya phir koi specific question hai? Main family members, AI identity, ya owner ke bare mein bata sakta hoon.";
    }
    
    private String detectLanguage(String text) {
        String lowerText = text.toLowerCase();
        
        // Hindi words in Roman script
        String[] hindiWords = {
            "kon", "kaun", "kya", "kahan", "kab", "kisne", "tum", "aap", "main", "mujhe", "mera", "meri", 
            "ham", "hum", "ho", "hai", "hain", "tha", "thi", "the", "banaya", "banayi", "banaye",
            "karna", "karti", "karte", "kar", "karne", "sakta", "sakti", "sakte", "sak", "sakne",
            "naam", "kaise", "kyun", "kahan", "kabhi", "kuch", "koi", "sab", "saare", "dono",
            "acha", "accha", "bura", "buri", "bada", "badi", "chota", "choti", "naya", "nayi",
            "purana", "purani", "acha", "accha", "bura", "buri", "meetha", "meethi", "teekha", "teekhi",
            "samajh", "samajhte", "samajhti", "samajhne", "dekh", "dekhte", "dekhti", "dekhne",
            "sun", "sunte", "sunti", "sunne", "bol", "bolte", "bolti", "bolne", "soch", "sochte", "sochti", "sochne"
        };
        
        // Check for Hindi words
        for (String hindiWord : hindiWords) {
            if (lowerText.contains(hindiWord)) {
                return "hindi";
            }
        }
        
        // Check for Devanagari characters
        if (Pattern.compile("[\\u0900-\\u097F]").matcher(text).find()) {
            return "hindi";
        } else if (Pattern.compile("[a-zA-Z]").matcher(text).find()) {
            return "english";
        } else {
            return "mixed";
        }
    }
    
    private double calculateConfidence(String question, String response) {
        // Higher confidence for specific responses
        if (response.contains(AI_KNOWLEDGE.get("creator")) || 
            response.contains(AI_KNOWLEDGE.get("owner_wife")) || 
            response.contains(AI_KNOWLEDGE.get("owner_mother")) || 
            response.contains(AI_KNOWLEDGE.get("owner_father")) || 
            response.contains(AI_KNOWLEDGE.get("owner_sister"))) {
            return 0.95;
        }
        
        // Medium confidence for general responses
        if (response.contains("Main") || response.contains("Mera") || response.contains("Mujhe")) {
            return 0.8;
        }
        
        // Lower confidence for fallback responses
        return 0.5;
    }
} 
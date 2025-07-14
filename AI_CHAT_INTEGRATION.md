# AI Chat Integration

This document describes the complete AI chat integration between the Angular frontend and Spring Boot backend.

## Features

### Frontend (Angular)
- **AI-themed chat interface** with futuristic design
- **Real-time messaging** with loading states
- **Speech-to-text** functionality (Hindi to English conversion)
- **Text-to-speech** for AI responses
- **Feedback system** with thumbs up/down buttons
- **Conversation management** with backend integration
- **Responsive design** for all devices

### Backend (Spring Boot)
- **AI Chat Service** with learning capabilities
- **Conversation management** with persistent storage
- **Feedback processing** for continuous improvement
- **Hindi/English mixed language support**
- **Performance statistics** and analytics
- **RESTful API** endpoints

## API Endpoints

### AI Chat Endpoints
- `POST /api/ai-chat/ask` - Send a question to AI
- `GET /api/ai-chat/conversations/{userId}` - Get user conversations
- `GET /api/ai-chat/conversation/{conversationId}/messages` - Get conversation messages
- `POST /api/ai-chat/feedback` - Submit feedback for AI response
- `GET /api/ai-chat/stats/{userId}` - Get AI performance statistics
- `DELETE /api/ai-chat/conversation/{conversationId}` - Delete conversation
- `DELETE /api/ai-chat/conversations/{userId}` - Clear all conversations

## Data Models

### Frontend Models
```typescript
interface ChatMessage {
  id: number;
  text: string;
  sender: 'user' | 'ai';
  timestamp: Date;
  isLoading?: boolean;
  aiResponseId?: string;
  conversationId?: string;
}

interface AiChatRequest {
  question: string;
  userId: string;
  conversationId?: string;
}

interface AiChatResponse {
  id: string;
  question: string;
  answer: string;
  conversationId: string;
  timestamp: string;
  confidence: number;
  language: string;
}
```

### Backend Models
```java
@Entity
public class AiConversation {
    private String id;
    private String userId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AiChatResponse> messages;
}

public class AiChatRequest {
    private String question;
    private String userId;
    private String conversationId;
}

public class AiChatResponse {
    private String id;
    private String question;
    private String answer;
    private String conversationId;
    private LocalDateTime timestamp;
    private double confidence;
    private String language;
}
```

## Key Features

### 1. Speech Integration
- **Speech Recognition**: Uses Web Speech API for voice input
- **Hindi to English**: Converts Hindi speech to English text using mapping dictionary
- **Text-to-Speech**: AI responses are spoken using natural Hindi/English voices
- **Voice Selection**: Automatically selects best available Hindi/Indian English voices

### 2. AI Learning System
- **Feedback Processing**: Users can rate AI responses (1-5 stars)
- **Continuous Learning**: Backend learns from feedback to improve responses
- **Performance Tracking**: Monitors AI accuracy and user satisfaction
- **Conversation Context**: Maintains conversation history for better responses

### 3. User Experience
- **Loading States**: Shows "AI is thinking..." with animated indicators
- **Real-time Updates**: Messages appear instantly with smooth animations
- **Error Handling**: Graceful error messages for network issues
- **Responsive Design**: Works perfectly on desktop, tablet, and mobile

### 4. Security & Performance
- **CORS Configuration**: Properly configured for frontend-backend communication
- **User Authentication**: Integrated with existing auth system
- **Data Persistence**: Conversations stored in database
- **Scalable Architecture**: Ready for production deployment

## Setup Instructions

### Backend Setup
1. Navigate to `Backend/end-microservice/`
2. Run `mvn spring-boot:run`
3. Backend will start on `http://localhost:8080`

### Frontend Setup
1. Navigate to `Ui/`
2. Run `npm install` (if not already done)
3. Run `npm start`
4. Frontend will start on `http://localhost:4200`

### Environment Configuration
- Backend API URL: `http://localhost:8080` (development)
- Update `Ui/src/environments/environment.prod.ts` for production

## Usage

1. **Login/Register** to access the chat
2. **Navigate to "Talk to AI"** from the navbar
3. **Type or speak** your questions in Hindi or English
4. **Get AI responses** with Hindi/English mixed language
5. **Provide feedback** using thumbs up/down buttons
6. **Clear chat** or start new conversations as needed

## Technical Implementation

### Frontend Services
- `AiChatService`: Handles all API communication
- `AuthService`: Manages user authentication
- Speech recognition and synthesis integration

### Backend Services
- `AiChatService`: Core AI logic and response generation
- `AiConversationRepository`: Database operations
- `AiChatRest`: REST API endpoints
- Learning algorithm for continuous improvement

### Database Schema
- `ai_conversations`: Stores conversation metadata
- `ai_chat_responses`: Stores individual messages
- `ai_feedback`: Stores user feedback for learning

## Future Enhancements

1. **Advanced AI Models**: Integration with GPT, Claude, or custom models
2. **Multi-language Support**: Support for more Indian languages
3. **File Upload**: Allow users to upload documents for analysis
4. **Voice Cloning**: Personalized AI voice based on user preference
5. **Analytics Dashboard**: Detailed insights into AI performance
6. **Real-time Collaboration**: Multiple users in same conversation

## Troubleshooting

### Common Issues
1. **CORS Errors**: Ensure backend CORS configuration is correct
2. **Speech Recognition**: Check browser permissions and HTTPS requirement
3. **API Connection**: Verify backend is running and accessible
4. **Voice Issues**: Check available system voices and permissions

### Debug Mode
- Frontend: Check browser console for detailed logs
- Backend: Check application logs for API requests and errors
- Database: Verify data persistence and connection

## Contributing

1. Follow existing code structure and naming conventions
2. Add proper error handling and validation
3. Update documentation for new features
4. Test thoroughly on different devices and browsers
5. Ensure accessibility compliance

---

This integration provides a complete, production-ready AI chat experience with modern UI/UX, robust backend services, and continuous learning capabilities. 
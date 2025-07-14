import { Component, OnInit, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { AiChatService, AiChatRequest, AiChatResponse } from '../../services/ai-chat.service';

interface ChatMessage {
  id: number;
  text: string;
  sender: 'user' | 'ai';
  timestamp: Date;
  isLoading?: boolean;
  aiResponseId?: string;
  conversationId?: string;
}

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, AfterViewChecked {
  @ViewChild('chatContainer') private chatContainer!: ElementRef;
  @ViewChild('messageInput') private messageInput!: ElementRef;

  messages: ChatMessage[] = [];
  newMessage: string = '';
  userData: any = null;
  isTyping: boolean = false;
  isListening: boolean = false;
  isSpeaking: boolean = false;
  recognition: any = null;
  synthesis: SpeechSynthesis | null = null;
  currentConversationId: string | null = null;
  isLoading: boolean = false;

  constructor(
    private authService: AuthService,
    private aiChatService: AiChatService
  ) {}

  ngOnInit(): void {
    this.userData = this.authService.getUserData();
    this.addWelcomeMessage();
    this.initializeSpeech();
    
    // Load voices when they become available
    if (this.synthesis) {
      this.synthesis.onvoiceschanged = () => {
        console.log('Voices loaded:', this.synthesis?.getVoices().length);
      };
    }
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  private addWelcomeMessage(): void {
    this.messages.push({
      id: 1,
      text: `Namaste ${this.userData?.firstName || this.userData?.username}! Main aapka AI assistant hoon. Aapko kya help chahiye aaj?`,
      sender: 'ai',
      timestamp: new Date()
    });
  }

  sendMessage(): void {
    if (this.newMessage.trim() && !this.isTyping && !this.isLoading) {
      const userMessage: ChatMessage = {
        id: this.messages.length + 1,
        text: this.newMessage.trim(),
        sender: 'user',
        timestamp: new Date()
      };

      this.messages.push(userMessage);
      const userInput = this.newMessage;
      this.newMessage = '';
      this.isTyping = true;
      this.isLoading = true;

      // Add loading message
      const loadingMessage: ChatMessage = {
        id: this.messages.length + 1,
        text: 'Thinking...',
        sender: 'ai',
        timestamp: new Date(),
        isLoading: true
      };
      this.messages.push(loadingMessage);

      // Send to AI backend
      const request: AiChatRequest = {
        question: userInput,
        userId: this.userData?.id?.toString() || this.userData?.username,
        conversationId: this.currentConversationId || undefined
      };

      this.aiChatService.sendQuestion(request).subscribe({
        next: (response: AiChatResponse) => {
          // Remove loading message
          this.messages = this.messages.filter(msg => !msg.isLoading);
          
          // Store conversation ID for future messages
          this.currentConversationId = response.conversationId?.toString() || null;
          
          // Add AI response
          const aiMessage: ChatMessage = {
            id: this.messages.length + 1,
            text: response.response,
            sender: 'ai',
            timestamp: new Date(),
            aiResponseId: response.conversationId?.toString() || '',
            conversationId: response.conversationId?.toString() || ''
          };

          this.messages.push(aiMessage);
          this.isTyping = false;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error getting AI response:', error);
          
          // Remove loading message
          this.messages = this.messages.filter(msg => !msg.isLoading);
          
          // Add error message
          const errorMessage: ChatMessage = {
            id: this.messages.length + 1,
            text: 'Sorry, main abhi aapki help nahi kar pa raha hoon. Please try again later.',
            sender: 'ai',
            timestamp: new Date()
          };

          this.messages.push(errorMessage);
          this.isTyping = false;
          this.isLoading = false;
        }
      });
    }
  }

  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  private scrollToBottom(): void {
    try {
      this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    } catch (err) {}
  }

  clearChat(): void {
    this.messages = [];
    this.currentConversationId = null;
    this.addWelcomeMessage();
  }

  focusInput(): void {
    setTimeout(() => {
      this.messageInput.nativeElement.focus();
    }, 100);
  }

  submitFeedback(questionId: string, conversationId: string, rating: number, feedback: string): void {
    const feedbackRequest = {
      conversationId: conversationId,
      questionId: questionId,
      rating: rating,
      feedback: feedback
    };

    this.aiChatService.submitFeedback(feedbackRequest).subscribe({
      next: (response) => {
        console.log('Feedback submitted successfully:', response);
        // Show a brief success message
        this.showFeedbackMessage('Thank you for your feedback!');
      },
      error: (error) => {
        console.error('Error submitting feedback:', error);
        this.showFeedbackMessage('Failed to submit feedback. Please try again.');
      }
    });
  }

  private showFeedbackMessage(message: string): void {
    // Create a temporary feedback message
    const feedbackMessage: ChatMessage = {
      id: this.messages.length + 1,
      text: message,
      sender: 'ai',
      timestamp: new Date()
    };

    this.messages.push(feedbackMessage);

    // Remove the feedback message after 3 seconds
    setTimeout(() => {
      this.messages = this.messages.filter(msg => msg.id !== feedbackMessage.id);
    }, 3000);
  }

  private initializeSpeech(): void {
    // Initialize speech synthesis
    if ('speechSynthesis' in window) {
      this.synthesis = window.speechSynthesis;
    }

    // Initialize speech recognition
    if ('webkitSpeechRecognition' in window) {
      this.recognition = new (window as any).webkitSpeechRecognition();
      this.recognition.continuous = false;
      this.recognition.interimResults = false;
      this.recognition.lang = 'hi-IN'; // Hindi (India)
      this.recognition.maxAlternatives = 3; // Get multiple recognition alternatives

      this.recognition.onstart = () => {
        this.isListening = true;
      };

      this.recognition.onresult = (event: any) => {
        const transcript = event.results[0][0].transcript;
        // Convert Hindi speech to English text
        const englishText = this.convertHindiToEnglish(transcript);
        this.newMessage = englishText;
        this.isListening = false;
      };

      this.recognition.onerror = (event: any) => {
        console.error('Speech recognition error:', event.error);
        this.isListening = false;
      };

      this.recognition.onend = () => {
        this.isListening = false;
      };
    }
  }

  startListening(): void {
    if (this.recognition && !this.isListening) {
      try {
        this.recognition.start();
      } catch (error) {
        console.error('Error starting speech recognition:', error);
        this.isListening = false;
      }
    }
  }

  stopListening(): void {
    if (this.recognition && this.isListening) {
      try {
        this.recognition.stop();
      } catch (error) {
        console.error('Error stopping speech recognition:', error);
      }
    }
  }

  speakMessage(text: string): void {
    if (this.synthesis) {
      try {
        // Stop any current speech
        this.synthesis.cancel();

        const utterance = new SpeechSynthesisUtterance(text);
        
        // Get available voices and select a clear Hindi/English voice
        const voices = this.synthesis.getVoices();
        const preferredVoices = [
          'hi-IN-NeerjaNeural', // Hindi Neural voice
          'en-IN-NeerjaNeural', // English (India) Neural voice
          'hi-IN-MadhurNeural', // Hindi Male Neural voice
          'en-IN-PrabhatNeural', // English (India) Male Neural voice
          'en-US-JennyNeural',   // Clear English voice
          'en-GB-SoniaNeural'    // British English voice
        ];

        // Find the best available voice
        let selectedVoice = null;
        for (const voiceName of preferredVoices) {
          selectedVoice = voices.find(voice => voice.name === voiceName);
          if (selectedVoice) break;
        }

        // If no preferred voice found, try to find any Hindi or Indian English voice
        if (!selectedVoice) {
          selectedVoice = voices.find(voice => 
            voice.lang.startsWith('hi-') || 
            voice.lang.startsWith('en-IN') ||
            voice.name.toLowerCase().includes('hindi') ||
            voice.name.toLowerCase().includes('india')
          );
        }

        // If still no voice found, use default
        if (selectedVoice) {
          utterance.voice = selectedVoice;
        }

        // Optimize for natural Hindi/English speech
        utterance.rate = 1.1;       // Faster, more natural speed
        utterance.pitch = 1.0;      // Normal pitch
        utterance.volume = 1.0;     // Full volume for clear audio
        utterance.lang = 'hi-IN';   // Set language to Hindi

        utterance.onstart = () => {
          this.isSpeaking = true;
        };

        utterance.onend = () => {
          this.isSpeaking = false;
        };

        utterance.onerror = (event) => {
          console.error('Speech synthesis error:', event);
          this.isSpeaking = false;
        };

        this.synthesis.speak(utterance);
      } catch (error) {
        console.error('Error with speech synthesis:', error);
        this.isSpeaking = false;
      }
    }
  }

  stopSpeaking(): void {
    if (this.synthesis) {
      try {
        this.synthesis.cancel();
        this.isSpeaking = false;
      } catch (error) {
        console.error('Error stopping speech synthesis:', error);
      }
    }
  }

  toggleSpeech(): void {
    if (this.isSpeaking) {
      this.stopSpeaking();
    } else {
      // Speak the last AI message
      const lastAiMessage = this.messages
        .filter(msg => msg.sender === 'ai')
        .pop();
      if (lastAiMessage) {
        this.speakMessage(lastAiMessage.text);
      }
    }
  }

  getAvailableVoices(): void {
    if (this.synthesis) {
      const voices = this.synthesis.getVoices();
      console.log('Available voices:');
      voices.forEach(voice => {
        console.log(`- ${voice.name} (${voice.lang})`);
      });
    }
  }

  private convertHindiToEnglish(hindiText: string): string {
    // Common Hindi to English word mappings
    const hindiToEnglish: { [key: string]: string } = {
      // Basic words
      'नमस्ते': 'namaste',
      'है': 'hai',
      'में': 'main',
      'आप': 'aap',
      'मुझे': 'mujhe',
      'क्या': 'kya',
      'कैसे': 'kaise',
      'कहाँ': 'kahan',
      'कब': 'kab',
      'कौन': 'kaun',
      
      // Common phrases
      'मदद': 'help',
      'समस्या': 'problem',
      'समाधान': 'solution',
      'जानकारी': 'information',
      'प्रश्न': 'question',
      'उत्तर': 'answer',
      'सहायता': 'assistance',
      'सेवा': 'service',
      
      // Actions
      'करना': 'karna',
      'जाना': 'jana',
      'आना': 'aana',
      'देखना': 'dekhna',
      'सुनना': 'sunna',
      'बोलना': 'bolna',
      'समझना': 'samajhna',
      'सोचना': 'sochna',
      
      // Time related
      'आज': 'aaj',
      'कल': 'kal',
      'परसों': 'parson',
      'सुबह': 'subah',
      'शाम': 'sham',
      'रात': 'raat',
      'दिन': 'din',
      
      // Numbers
      'एक': 'ek',
      'दो': 'do',
      'तीन': 'teen',
      'चार': 'char',
      'पांच': 'paanch',
      'छह': 'cheh',
      'सात': 'saat',
      'आठ': 'aath',
      'नौ': 'nau',
      'दस': 'das',
      
      // Common expressions
      'धन्यवाद': 'thank you',
      'कृपया': 'please',
      'माफ़ करें': 'sorry',
      'ठीक है': 'okay',
      'हाँ': 'yes',
      'नहीं': 'no',
      'बहुत': 'bahut',
      'अच्छा': 'accha',
      'बुरा': 'bura',
      
      // Technology terms
      'कंप्यूटर': 'computer',
      'मोबाइल': 'mobile',
      'इंटरनेट': 'internet',
      'वेबसाइट': 'website',
      'एप्लिकेशन': 'application',
      'सिस्टम': 'system',
      'डेटा': 'data',
      'फ़ाइल': 'file',
      
      // Business terms
      'कंपनी': 'company',
      'कार्यालय': 'office',
      'कार्य': 'work',
      'पैसा': 'money',
      'लेनदेन': 'transaction',
      'खाता': 'account',
      'बैंक': 'bank',
      
      // Family and relationships
      'परिवार': 'family',
      'दोस्त': 'friend',
      'भाई': 'bhai',
      'बहन': 'behen',
      'माता': 'mata',
      'पिता': 'pita',
      'बेटा': 'beta',
      'बेटी': 'beti'
    };

    let englishText = hindiText;
    
    // Convert Hindi words to English
    for (const [hindi, english] of Object.entries(hindiToEnglish)) {
      const regex = new RegExp(hindi, 'gi');
      englishText = englishText.replace(regex, english);
    }
    
    // Handle common Hindi patterns
    englishText = englishText
      .replace(/हूँ/g, 'hoon')
      .replace(/हैं/g, 'hain')
      .replace(/था/g, 'tha')
      .replace(/थी/g, 'thi')
      .replace(/थे/g, 'the')
      .replace(/रहा/g, 'raha')
      .replace(/रही/g, 'rahi')
      .replace(/रहे/g, 'rahe')
      .replace(/कर/g, 'kar')
      .replace(/की/g, 'ki')
      .replace(/का/g, 'ka')
      .replace(/के/g, 'ke')
      .replace(/को/g, 'ko')
      .replace(/से/g, 'se')
      .replace(/में/g, 'main')
      .replace(/पर/g, 'par')
      .replace(/तक/g, 'tak')
      .replace(/साथ/g, 'saath')
      .replace(/बिना/g, 'bina')
      .replace(/लिए/g, 'liye')
      .replace(/द्वारा/g, 'dwara')
      .replace(/के लिए/g, 'ke liye')
      .replace(/की तरह/g, 'ki tarah')
      .replace(/के बारे में/g, 'ke bare mein')
      .replace(/के साथ/g, 'ke saath')
      .replace(/के बिना/g, 'ke bina')
      .replace(/के बाद/g, 'ke baad')
      .replace(/के पहले/g, 'ke pehle')
      .replace(/के अंदर/g, 'ke andar')
      .replace(/के बाहर/g, 'ke bahar')
      .replace(/के ऊपर/g, 'ke upar')
      .replace(/के नीचे/g, 'ke neeche')
      .replace(/के आगे/g, 'ke aage')
      .replace(/के पीछे/g, 'ke peeche')
      .replace(/के दाएं/g, 'ke daayein')
      .replace(/के बाएं/g, 'ke baayein');

    return englishText;
  }
} 
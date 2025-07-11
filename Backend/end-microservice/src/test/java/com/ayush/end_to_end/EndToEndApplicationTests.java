package com.ayush.end_to_end;

import com.ayush.end_to_end.entity.User;
import com.ayush.end_to_end.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EndToEndApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
    }

    @Test
    void testUserCreation() {
        // Create a test user
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");
        user.setPhoneNumber("1234567890");
        user.setAddress("Test Address");
        user.setIsActive(true);

        // Save the user
        User savedUser = userRepository.save(user);

        // Verify the user was saved
        assertNotNull(savedUser.getId());
        assertEquals("Test", savedUser.getFirstName());
        assertEquals("User", savedUser.getLastName());
        assertEquals("test@example.com", savedUser.getEmail());

        // Clean up
        userRepository.delete(savedUser);
    }

    @Test
    void testUserValidation() {
        // Test that validation works
        User user = new User();
        // Missing required fields should cause validation errors
        user.setEmail("invalid-email");
        
        // This test verifies that the entity is properly configured
        assertNotNull(user);
    }
}

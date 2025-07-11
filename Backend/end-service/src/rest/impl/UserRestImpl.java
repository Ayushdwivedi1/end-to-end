package rest.impl;

import com.ayush.end_to_end.dto.ApiResponse;
import com.ayush.end_to_end.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import service.UserService;
import userRest.UserRest;

import java.util.List;

@Service
public class UserRestImpl implements UserRest {
    
    private static final Logger log = LoggerFactory.getLogger(UserRestImpl.class);
    
    @Autowired
    private UserService userService;
    
    @Override
    public ResponseEntity<String> test() {
        log.info("Test endpoint called");
        return ResponseEntity.ok("Controller is working!");
    }
    
    @Override
    public ResponseEntity<ApiResponse<UserDto>> createUser(UserDto userDto) {
        log.info("Creating new user with email: {}", userDto.getEmail());
        UserDto createdUser = userService.createUser(userDto);
        log.info("User created successfully with ID: {}", createdUser.getId());
        return ResponseEntity.ok(ApiResponse.success("User created successfully", createdUser));
    }
    
    @Override
    public ResponseEntity<ApiResponse<UserDto>> getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        UserDto user = userService.getUserById(id);
        log.info("User retrieved successfully for ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }
    
    @Override
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        UserDto user = userService.getUserByEmail(email);
        log.info("User retrieved successfully for email: {}", email);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }
    
    @Override
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        log.info("Fetching all users");
        List<UserDto> users = userService.getAllUsers();
        log.info("Retrieved {} users successfully", users.size());
        return ResponseEntity.ok(ApiResponse.success("All users retrieved successfully", users));
    }
    
    @Override
    public ResponseEntity<ApiResponse<List<UserDto>>> getActiveUsers() {
        log.info("Fetching active users");
        List<UserDto> users = userService.getActiveUsers();
        log.info("Retrieved {} active users successfully", users.size());
        return ResponseEntity.ok(ApiResponse.success("Active users retrieved successfully", users));
    }
    
    @Override
    public ResponseEntity<ApiResponse<List<UserDto>>> searchUsersByName(String name) {
        log.info("Searching users by name: {}", name);
        List<UserDto> users = userService.searchUsersByName(name);
        log.info("Found {} users matching name: {}", users.size(), name);
        return ResponseEntity.ok(ApiResponse.success("Users found successfully", users));
    }
    
    @Override
    public ResponseEntity<ApiResponse<UserDto>> updateUser(Long id, UserDto userDto) {
        log.info("Updating user with ID: {}", id);
        UserDto updatedUser = userService.updateUser(id, userDto);
        log.info("User updated successfully for ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUser(id);
        log.info("User deleted successfully for ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> deactivateUser(Long id) {
        log.info("Deactivating user with ID: {}", id);
        userService.deactivateUser(id);
        log.info("User deactivated successfully for ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully"));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> activateUser(Long id) {
        log.info("Activating user with ID: {}", id);
        userService.activateUser(id);
        log.info("User activated successfully for ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully"));
    }
}

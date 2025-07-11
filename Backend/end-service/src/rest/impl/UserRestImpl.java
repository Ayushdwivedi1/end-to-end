package rest.impl;

import com.ayush.end_to_end.dto.ApiResponse;
import com.ayush.end_to_end.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import service.UserService;
import userRest.UserRest;

import java.util.List;

@Service
public class UserRestImpl implements UserRest {
    
    @Autowired
    private UserService userService;
    
    @Override
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Controller is working!");
    }
    
    @Override
    public ResponseEntity<ApiResponse<UserDto>> createUser(UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.ok(ApiResponse.success("User created successfully", createdUser));
    }
    
    @Override
    public ResponseEntity<ApiResponse<UserDto>> getUserById(Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }
    
    @Override
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail(String email) {
        UserDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }
    
    @Override
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("All users retrieved successfully", users));
    }
    
    @Override
    public ResponseEntity<ApiResponse<List<UserDto>>> getActiveUsers() {
        List<UserDto> users = userService.getActiveUsers();
        return ResponseEntity.ok(ApiResponse.success("Active users retrieved successfully", users));
    }
    
    @Override
    public ResponseEntity<ApiResponse<List<UserDto>>> searchUsersByName(String name) {
        List<UserDto> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(ApiResponse.success("Users found successfully", users));
    }
    
    @Override
    public ResponseEntity<ApiResponse<UserDto>> updateUser(Long id, UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> deleteUser(Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> deactivateUser(Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully"));
    }
    
    @Override
    public ResponseEntity<ApiResponse<String>> activateUser(Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully"));
    }
}

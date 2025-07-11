package userRest;

import com.ayush.end_to_end.dto.ApiResponse;
import com.ayush.end_to_end.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public interface UserRest {
    
    @GetMapping("/test")
    ResponseEntity<String> test();
    
    @PostMapping("/users")
    ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserDto userDto);
    
    @GetMapping("/users/{id}")
    ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id);
    
    @GetMapping("/users/email/{email}")
    ResponseEntity<ApiResponse<UserDto>> getUserByEmail(@PathVariable String email);
    
    @GetMapping("/users")
    ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers();
    
    @GetMapping("/users/active")
    ResponseEntity<ApiResponse<List<UserDto>>> getActiveUsers();
    
    @GetMapping("/users/search")
    ResponseEntity<ApiResponse<List<UserDto>>> searchUsersByName(@RequestParam String name);
    
    @PutMapping("/users/{id}")
    ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable Long id, @RequestBody UserDto userDto);
    
    @DeleteMapping("/users/{id}")
    ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id);
    
    @PatchMapping("/users/{id}/deactivate")
    ResponseEntity<ApiResponse<String>> deactivateUser(@PathVariable Long id);
    
    @PatchMapping("/users/{id}/activate")
    ResponseEntity<ApiResponse<String>> activateUser(@PathVariable Long id);
}

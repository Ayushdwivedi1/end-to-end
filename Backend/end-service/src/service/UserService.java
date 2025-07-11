package service;

import com.ayush.end_to_end.dto.UserDto;
import com.ayush.end_to_end.dto.UserUpdateDto;
import java.util.List;

public interface UserService {
    
    UserDto createUser(UserDto userDto);
    
    UserDto getUserById(Long id);
    
    UserDto getUserByEmail(String email);
    
    List<UserDto> getAllUsers();
    
    List<UserDto> getActiveUsers();
    
    List<UserDto> searchUsersByName(String name);
    
    UserDto updateUser(Long id, UserUpdateDto userUpdateDto);
    
    void deleteUser(Long id);
    
    void deactivateUser(Long id);
    
    void activateUser(Long id);
}

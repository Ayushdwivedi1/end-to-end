package service.serviceImpl;

import com.ayush.end_to_end.dto.UserDto;
import com.ayush.end_to_end.dto.UserUpdateDto;
import com.ayush.end_to_end.entity.User;
import com.ayush.end_to_end.exception.UserAlreadyExistsException;
import com.ayush.end_to_end.exception.UserNotFoundException;
import com.ayush.end_to_end.mapper.UserMapper;
import com.ayush.end_to_end.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements service.UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("Starting user creation process for email: {}", userDto.getEmail());
        
        // Check if user with email already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            log.info("User creation failed - email already exists: {}", userDto.getEmail());
            throw new UserAlreadyExistsException(userDto.getEmail());
        }
        
        log.info("Email validation passed, proceeding with user creation");
        User user = userMapper.toEntity(userDto);
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {} and email: {}", savedUser.getId(), savedUser.getEmail());
        return userMapper.toDto(savedUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        User user = userRepository.findActiveUserById(id)
                .orElseThrow(() -> {
                    log.info("User not found for ID: {}", id);
                    return new UserNotFoundException(id);
                });
        log.info("User found for ID: {} - email: {}", id, user.getEmail());
        return userMapper.toDto(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        User user = userRepository.findActiveUserByEmail(email)
                .orElseThrow(() -> {
                    log.info("User not found for email: {}", email);
                    return new UserNotFoundException("email", email);
                });
        log.info("User found for email: {} - ID: {}", email, user.getId());
        return userMapper.toDto(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users from database");
        List<User> users = userRepository.findAll();
        log.info("Retrieved {} total users from database", users.size());
        return userMapper.toDtoList(users);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getActiveUsers() {
        log.info("Fetching active users from database");
        List<User> users = userRepository.findByIsActiveTrue();
        log.info("Retrieved {} active users from database", users.size());
        return userMapper.toDtoList(users);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> searchUsersByName(String name) {
        log.info("Searching users by name: {}", name);
        List<User> users = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        log.info("Found {} users matching name: {}", users.size(), name);
        return userMapper.toDtoList(users);
    }
    
    @Override
    public UserDto updateUser(Long id, UserUpdateDto userUpdateDto) {
        log.info("Starting user update process for ID: {}", id);
        
        User existingUser = userRepository.findActiveUserById(id)
                .orElseThrow(() -> {
                    log.info("User not found for update - ID: {}", id);
                    return new UserNotFoundException(id);
                });
        
        log.info("Found existing user for update - ID: {}, current email: {}", id, existingUser.getEmail());
        
        // Check if email is being changed and if it already exists
        if (userUpdateDto.getEmail() != null && !existingUser.getEmail().equals(userUpdateDto.getEmail()) && 
            userRepository.existsByEmailAndIdNot(userUpdateDto.getEmail(), id)) {
            log.info("User update failed - new email already exists: {}", userUpdateDto.getEmail());
            throw new UserAlreadyExistsException(userUpdateDto.getEmail());
        }
        
        log.info("Email validation passed, proceeding with user update");
        
        // Update fields only if they are not null (partial update)
        if (userUpdateDto.getFirstName() != null) {
            existingUser.setFirstName(userUpdateDto.getFirstName());
        }
        if (userUpdateDto.getLastName() != null) {
            existingUser.setLastName(userUpdateDto.getLastName());
        }
        if (userUpdateDto.getEmail() != null) {
            existingUser.setEmail(userUpdateDto.getEmail());
        }
        if (userUpdateDto.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(userUpdateDto.getPhoneNumber());
        }
        if (userUpdateDto.getAddress() != null) {
            existingUser.setAddress(userUpdateDto.getAddress());
        }
        
        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully - ID: {}, email: {}", updatedUser.getId(), updatedUser.getEmail());
        return userMapper.toDto(updatedUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        log.info("Starting user deletion process for ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User not found for deletion - ID: {}", id);
                    return new UserNotFoundException(id);
                });
        userRepository.delete(user);
        log.info("User deleted successfully - ID: {}", id);
    }
    
    @Override
    public void deactivateUser(Long id) {
        log.info("Starting user deactivation process for ID: {}", id);
        User user = userRepository.findActiveUserById(id)
                .orElseThrow(() -> {
                    log.info("Active user not found for deactivation - ID: {}", id);
                    return new UserNotFoundException(id);
                });
        user.setIsActive(false);
        userRepository.save(user);
        log.info("User deactivated successfully - ID: {}", id);
    }
    
    @Override
    public void activateUser(Long id) {
        log.info("Starting user activation process for ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("User not found for activation - ID: {}", id);
                    return new UserNotFoundException(id);
                });
        user.setIsActive(true);
        userRepository.save(user);
        log.info("User activated successfully - ID: {}", id);
    }
}

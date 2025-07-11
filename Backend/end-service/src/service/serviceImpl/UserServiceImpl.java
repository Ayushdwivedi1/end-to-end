package service.serviceImpl;

import com.ayush.end_to_end.dto.UserDto;
import com.ayush.end_to_end.entity.User;
import com.ayush.end_to_end.exception.UserAlreadyExistsException;
import com.ayush.end_to_end.exception.UserNotFoundException;
import com.ayush.end_to_end.mapper.UserMapper;
import com.ayush.end_to_end.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements service.UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public UserDto createUser(UserDto userDto) {
        // Check if user with email already exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException(userDto.getEmail());
        }
        
        User user = userMapper.toEntity(userDto);
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findActiveUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email", email));
        return userMapper.toDto(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getActiveUsers() {
        List<User> users = userRepository.findByIsActiveTrue();
        return userMapper.toDtoList(users);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> searchUsersByName(String name) {
        List<User> users = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        return userMapper.toDtoList(users);
    }
    
    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(userDto.getEmail()) && 
            userRepository.existsByEmailAndIdNot(userDto.getEmail(), id)) {
            throw new UserAlreadyExistsException(userDto.getEmail());
        }
        
        // Update fields
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhoneNumber(userDto.getPhoneNumber());
        existingUser.setAddress(userDto.getAddress());
        
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }
    
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }
    
    @Override
    public void deactivateUser(Long id) {
        User user = userRepository.findActiveUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setIsActive(false);
        userRepository.save(user);
    }
    
    @Override
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setIsActive(true);
        userRepository.save(user);
    }
}

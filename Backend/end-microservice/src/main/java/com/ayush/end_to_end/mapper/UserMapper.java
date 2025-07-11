package com.ayush.end_to_end.mapper;

import com.ayush.end_to_end.dto.UserDto;
import com.ayush.end_to_end.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    
    private static final Logger log = LoggerFactory.getLogger(UserMapper.class);
    
    public UserDto toDto(User user) {
        if (user == null) {
            log.info("Converting null User entity to DTO");
            return null;
        }
        
        log.info("Converting User entity to DTO - ID: {}, email: {}", user.getId(), user.getEmail());
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setIsActive(user.getIsActive());
        
        log.info("User entity converted to DTO successfully - ID: {}", user.getId());
        return dto;
    }
    
    public User toEntity(UserDto dto) {
        if (dto == null) {
            log.info("Converting null UserDto to entity");
            return null;
        }
        
        log.info("Converting UserDto to entity - ID: {}, email: {}", dto.getId(), dto.getEmail());
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        
        log.info("UserDto converted to entity successfully - ID: {}", dto.getId());
        return user;
    }
    
    public List<UserDto> toDtoList(List<User> users) {
        if (users == null) {
            log.info("Converting null User list to DTO list");
            return null;
        }
        
        log.info("Converting {} User entities to DTO list", users.size());
        List<UserDto> dtoList = users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        log.info("User list converted to DTO list successfully - {} items", dtoList.size());
        return dtoList;
    }
    
    public List<User> toEntityList(List<UserDto> dtos) {
        if (dtos == null) {
            log.info("Converting null UserDto list to entity list");
            return null;
        }
        
        log.info("Converting {} UserDto objects to entity list", dtos.size());
        List<User> entityList = dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        log.info("UserDto list converted to entity list successfully - {} items", entityList.size());
        return entityList;
    }
} 
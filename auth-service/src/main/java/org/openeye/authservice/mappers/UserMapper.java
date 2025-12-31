package org.openeye.authservice.mappers;

import org.openeye.authservice.dao.entities.User;
import org.openeye.authservice.dtos.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return UserDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .build();
    }
}

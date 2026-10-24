
package com.vtcampus.lostandfound.mappers;

import com.vtcampus.lostandfound.domain.dto.UserRegistrationRequest;
import com.vtcampus.lostandfound.domain.dto.UserResponse;
import com.vtcampus.lostandfound.domain.entities.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Convert registration DTO into a Users entity
    public Users toEntity(UserRegistrationRequest request) {
        Users user = new Users();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    // Convert Users entity into a response DTO
    public UserResponse toResponse(Users user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}

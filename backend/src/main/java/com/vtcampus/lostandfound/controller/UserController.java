
package com.vtcampus.lostandfound.controller;

import com.vtcampus.lostandfound.domain.dto.UserRegistrationRequest;
import com.vtcampus.lostandfound.domain.dto.UserResponse;
import com.vtcampus.lostandfound.domain.entities.Users;
import com.vtcampus.lostandfound.mappers.UserMapper;
import com.vtcampus.lostandfound.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserRegistrationRequest request) {

        Users user = userMapper.toEntity(request);
        Users savedUser = userService.createUser(user);
        UserResponse response = userMapper.toResponse(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

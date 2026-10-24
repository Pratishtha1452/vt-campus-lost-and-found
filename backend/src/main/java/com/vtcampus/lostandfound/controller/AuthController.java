
package com.vtcampus.lostandfound.controller;

import com.vtcampus.lostandfound.domain.dto.LoginRequest;
import com.vtcampus.lostandfound.domain.dto.UserResponse;
import com.vtcampus.lostandfound.domain.entities.Users;
import com.vtcampus.lostandfound.mappers.UserMapper;
import com.vtcampus.lostandfound.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    public AuthController(
            AuthService authService,
            UserMapper userMapper) {
        this.authService = authService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @Valid @RequestBody LoginRequest request) {

        Users user = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(userMapper.toResponse(user));
    }
}

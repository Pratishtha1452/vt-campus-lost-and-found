
package com.vtcampus.lostandfound.controller;

import com.vtcampus.lostandfound.domain.dto.LoginRequest;
import com.vtcampus.lostandfound.domain.dto.UserResponse;
import com.vtcampus.lostandfound.domain.entities.Users;
import com.vtcampus.lostandfound.mappers.UserMapper;
import com.vtcampus.lostandfound.services.AuthService;
import com.vtcampus.lostandfound.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthController(
            AuthService authService,
            JwtService jwtService,
            UserMapper userMapper) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        Users user = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        String token = jwtService.generateToken(user);

        LoginResponse response = new LoginResponse(
                token,
                "Bearer",
                userMapper.toResponse(user)
        );

        return ResponseEntity.ok(response);
    }

    public record LoginResponse(
            String token,
            String tokenType,
            UserResponse user
    ) {}
}

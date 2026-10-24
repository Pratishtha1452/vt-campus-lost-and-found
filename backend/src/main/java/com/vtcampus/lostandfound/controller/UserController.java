
package com.vtcampus.lostandfound.controller;

import com.vtcampus.lostandfound.domain.dto.ChangePasswordRequest;
import com.vtcampus.lostandfound.domain.dto.UserRegistrationRequest;
import com.vtcampus.lostandfound.domain.dto.UserResponse;
import com.vtcampus.lostandfound.domain.entities.Users;
import com.vtcampus.lostandfound.mappers.UserMapper;
import com.vtcampus.lostandfound.services.UserService;
import com.vtcampus.lostandfound.services.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final CurrentUserService currentUserService;

    public UserController(
            UserService userService,
            UserMapper userMapper,
            CurrentUserService currentUserService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody UserRegistrationRequest request) {

        Users user = userMapper.toEntity(request);
        Users savedUser = userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toResponse(savedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id) {

        checkOwnership(id);

        return userService.viewUserById(id)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        checkOwnership(id);

        Users user = new Users();
        user.setName(request.getName());

        Users updatedUser = userService.updateUser(id, user);

        return ResponseEntity.ok(userMapper.toResponse(updatedUser));
    }

    @PatchMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {

        checkOwnership(id);

        userService.changePassword(
                id,
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        checkOwnership(id);

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    private void checkOwnership(Long requestedUserId) {
        Users currentUser = currentUserService.getCurrentUser();

        if (!currentUser.getId().equals(requestedUserId)) {
            throw new AccessDeniedException(
                    "You cannot access or modify another user's account");
        }
    }

    public static class UpdateUserRequest {

        @jakarta.validation.constraints.NotBlank(
                message = "Name is required")
        @jakarta.validation.constraints.Size(
                max = 100,
                message = "Name must not exceed 100 characters")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

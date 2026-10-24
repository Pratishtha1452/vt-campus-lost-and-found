
package com.vtcampus.lostandfound.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}

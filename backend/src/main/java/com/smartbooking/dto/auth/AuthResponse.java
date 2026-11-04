package com.smartbooking.dto.auth;

import com.smartbooking.dto.user.UserResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String token;
    private String tokenType;
    private UserResponse user;
}

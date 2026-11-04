package com.smartbooking.security;

import com.smartbooking.entity.User;
import com.smartbooking.exception.UnauthorizedException;
import com.smartbooking.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String currentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new UnauthorizedException("Not authenticated");
        }
        return authentication.getName();
    }

    public User currentUser() {
        return userRepository.findByEmail(currentEmail())
                .orElseThrow(() -> new UnauthorizedException("Authenticated user not found"));
    }
}

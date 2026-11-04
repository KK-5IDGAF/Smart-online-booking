package com.smartbooking.service;

import com.smartbooking.dto.user.UpdateProfileRequest;
import com.smartbooking.dto.user.UserResponse;
import com.smartbooking.entity.User;
import com.smartbooking.mapper.UserMapper;
import com.smartbooking.repository.UserRepository;
import com.smartbooking.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SecurityUtils securityUtils;

    public UserService(UserRepository userRepository, UserMapper userMapper, SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.securityUtils = securityUtils;
    }

    public UserResponse getProfile() {
        return userMapper.toResponse(securityUtils.currentUser());
    }

    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = securityUtils.currentUser();
        user.setName(request.getName());
        return userMapper.toResponse(userRepository.save(user));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }
}

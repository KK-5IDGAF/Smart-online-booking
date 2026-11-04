package com.smartbooking.service;

import com.smartbooking.dto.auth.RegisterRequest;
import com.smartbooking.dto.user.UserResponse;
import com.smartbooking.entity.Role;
import com.smartbooking.entity.User;
import com.smartbooking.mapper.UserMapper;
import com.smartbooking.repository.UserRepository;
import com.smartbooking.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerRejectsDuplicateEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Alex");
        request.setEmail("alex@test.com");
        request.setPassword("Password1");
        when(userRepository.existsByEmail("alex@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    void registerCreatesCustomer() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Alex");
        request.setEmail("alex@test.com");
        request.setPassword("Password1");
        when(userRepository.existsByEmail("alex@test.com")).thenReturn(false);
        when(passwordEncoder.encode("Password1")).thenReturn("hash");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtService.generateToken(any(), any())).thenReturn("token");
        when(userMapper.toResponse(any())).thenReturn(UserResponse.builder().id(1L).role(Role.CUSTOMER).build());

        var response = authService.register(request);
        assertEquals("token", response.getToken());
        assertEquals(Role.CUSTOMER, response.getUser().getRole());
    }
}

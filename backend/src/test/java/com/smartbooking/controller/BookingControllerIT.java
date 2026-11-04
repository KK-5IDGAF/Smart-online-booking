package com.smartbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbooking.entity.*;
import com.smartbooking.repository.ResourceRepository;
import com.smartbooking.repository.SlotRepository;
import com.smartbooking.repository.UserRepository;
import com.smartbooking.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BookingControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private ResourceRepository resourceRepository;
    @Autowired private SlotRepository slotRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;

    private String token;
    private Long availableSlotId;
    private Long takenSlotId;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(User.builder()
                .name("Casey")
                .email("casey@test.com")
                .passwordHash(passwordEncoder.encode("Password1"))
                .role(Role.CUSTOMER)
                .build());
        token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        Resource resource = resourceRepository.save(Resource.builder()
                .name("Lab")
                .type("ROOM")
                .description("Test lab")
                .capacity(8)
                .build());

        Slot open = slotRepository.save(Slot.builder()
                .resource(resource)
                .slotDate(LocalDate.now().plusDays(2))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(11, 0))
                .available(true)
                .build());
        Slot taken = slotRepository.save(Slot.builder()
                .resource(resource)
                .slotDate(LocalDate.now().plusDays(2))
                .startTime(LocalTime.of(11, 0))
                .endTime(LocalTime.of(12, 0))
                .available(false)
                .build());
        availableSlotId = open.getId();
        takenSlotId = taken.getId();
    }

    @Test
    void bookingUnavailableSlotReturns409() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("slotId", takenSlotId))))
                .andExpect(status().isConflict());
    }

    @Test
    void bookingAvailableSlotReturns201() throws Exception {
        mockMvc.perform(post("/api/bookings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(java.util.Map.of("slotId", availableSlotId))))
                .andExpect(status().isCreated());
    }
}

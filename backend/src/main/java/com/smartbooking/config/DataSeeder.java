package com.smartbooking.config;

import com.smartbooking.entity.*;
import com.smartbooking.repository.BookingRepository;
import com.smartbooking.repository.ResourceRepository;
import com.smartbooking.repository.SlotRepository;
import com.smartbooking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
@org.springframework.context.annotation.Profile("!test")
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository,
            ResourceRepository resourceRepository,
            SlotRepository slotRepository,
            BookingRepository bookingRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            User admin = userRepository.save(User.builder()
                    .name("System Admin")
                    .email("admin@smartbooking.com")
                    .passwordHash(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .build());

            userRepository.save(User.builder()
                    .name("Front Desk Staff")
                    .email("staff@smartbooking.com")
                    .passwordHash(passwordEncoder.encode("Staff@123"))
                    .role(Role.STAFF)
                    .build());

            User customer = userRepository.save(User.builder()
                    .name("Alex Customer")
                    .email("customer@smartbooking.com")
                    .passwordHash(passwordEncoder.encode("Customer@123"))
                    .role(Role.CUSTOMER)
                    .build());

            Resource hall = resourceRepository.save(Resource.builder()
                    .name("Conference Hall A")
                    .type("HALL")
                    .description("Air-conditioned hall with projector and 40 seats")
                    .capacity(40)
                    .build());

            Resource court = resourceRepository.save(Resource.builder()
                    .name("Tennis Court 1")
                    .type("SPORTS")
                    .description("Outdoor hard court with floodlights")
                    .capacity(4)
                    .build());

            Resource cabin = resourceRepository.save(Resource.builder()
                    .name("Consultation Cabin")
                    .type("ROOM")
                    .description("Private cabin for 1:1 appointments")
                    .capacity(2)
                    .build());

            LocalDate today = LocalDate.now().plusDays(1);
            Slot morning = slotRepository.save(Slot.builder()
                    .resource(hall)
                    .slotDate(today)
                    .startTime(LocalTime.of(9, 0))
                    .endTime(LocalTime.of(11, 0))
                    .available(false)
                    .build());
            slotRepository.saveAll(List.of(
                    Slot.builder().resource(hall).slotDate(today).startTime(LocalTime.of(11, 0)).endTime(LocalTime.of(13, 0)).available(true).build(),
                    Slot.builder().resource(hall).slotDate(today).startTime(LocalTime.of(14, 0)).endTime(LocalTime.of(16, 0)).available(true).build(),
                    Slot.builder().resource(court).slotDate(today).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(9, 30)).available(true).build(),
                    Slot.builder().resource(court).slotDate(today).startTime(LocalTime.of(10, 0)).endTime(LocalTime.of(11, 30)).available(true).build(),
                    Slot.builder().resource(cabin).slotDate(today).startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(9, 30)).available(true).build(),
                    Slot.builder().resource(cabin).slotDate(today).startTime(LocalTime.of(9, 30)).endTime(LocalTime.of(10, 0)).available(true).build()
            ));

            bookingRepository.save(Booking.builder()
                    .user(customer)
                    .slot(morning)
                    .status(BookingStatus.CONFIRMED)
                    .build());
        };
    }
}

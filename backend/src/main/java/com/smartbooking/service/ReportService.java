package com.smartbooking.service;

import com.smartbooking.dto.admin.DashboardStatsResponse;
import com.smartbooking.entity.BookingStatus;
import com.smartbooking.repository.BookingRepository;
import com.smartbooking.repository.ResourceRepository;
import com.smartbooking.repository.SlotRepository;
import com.smartbooking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final SlotRepository slotRepository;
    private final BookingRepository bookingRepository;

    public ReportService(
            UserRepository userRepository,
            ResourceRepository resourceRepository,
            SlotRepository slotRepository,
            BookingRepository bookingRepository
    ) {
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
        this.slotRepository = slotRepository;
        this.bookingRepository = bookingRepository;
    }

    public DashboardStatsResponse dashboard() {
        return DashboardStatsResponse.builder()
                .totalUsers(userRepository.count())
                .totalResources(resourceRepository.count())
                .totalSlots(slotRepository.count())
                .totalBookings(bookingRepository.count())
                .activeBookings(bookingRepository.countByStatusIn(List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED)))
                .cancelledBookings(bookingRepository.countByStatus(BookingStatus.CANCELLED))
                .pendingBookings(bookingRepository.countByStatus(BookingStatus.PENDING))
                .confirmedBookings(bookingRepository.countByStatus(BookingStatus.CONFIRMED))
                .build();
    }
}

package com.smartbooking.service;

import com.smartbooking.entity.BookingStatus;
import com.smartbooking.repository.BookingRepository;
import com.smartbooking.repository.ResourceRepository;
import com.smartbooking.repository.SlotRepository;
import com.smartbooking.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private ResourceRepository resourceRepository;
    @Mock private SlotRepository slotRepository;
    @Mock private BookingRepository bookingRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void dashboardAggregatesCounts() {
        when(userRepository.count()).thenReturn(10L);
        when(resourceRepository.count()).thenReturn(4L);
        when(slotRepository.count()).thenReturn(20L);
        when(bookingRepository.count()).thenReturn(7L);
        when(bookingRepository.countByStatusIn(java.util.List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED))).thenReturn(5L);
        when(bookingRepository.countByStatus(BookingStatus.CANCELLED)).thenReturn(2L);
        when(bookingRepository.countByStatus(BookingStatus.PENDING)).thenReturn(1L);
        when(bookingRepository.countByStatus(BookingStatus.CONFIRMED)).thenReturn(4L);

        var stats = reportService.dashboard();
        assertEquals(10L, stats.getTotalUsers());
        assertEquals(5L, stats.getActiveBookings());
        assertEquals(2L, stats.getCancelledBookings());
    }
}

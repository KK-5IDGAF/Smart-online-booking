package com.smartbooking.service;

import com.smartbooking.dto.booking.BookingRequest;
import com.smartbooking.entity.*;
import com.smartbooking.exception.SlotUnavailableException;
import com.smartbooking.mapper.BookingMapper;
import com.smartbooking.notification.NotificationDispatcher;
import com.smartbooking.repository.BookingRepository;
import com.smartbooking.repository.SlotRepository;
import com.smartbooking.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private SlotRepository slotRepository;
    @Mock private BookingMapper bookingMapper;
    @Mock private SecurityUtils securityUtils;
    @Mock private NotificationDispatcher notificationDispatcher;

    @InjectMocks
    private BookingService bookingService;

    private User customer;
    private Slot slot;
    private BookingRequest request;

    @BeforeEach
    void setUp() {
        customer = User.builder().id(1L).name("Alex").email("a@test.com").role(Role.CUSTOMER).build();
        Resource resource = Resource.builder().id(10L).name("Hall").type("HALL").capacity(10).build();
        slot = Slot.builder()
                .id(5L)
                .resource(resource)
                .slotDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 0))
                .available(true)
                .build();
        request = new BookingRequest();
        request.setSlotId(5L);
        when(securityUtils.currentUser()).thenReturn(customer);
    }

    @Test
    void createThrowsConflictWhenSlotUnavailable() {
        slot.setAvailable(false);
        when(slotRepository.findByIdForUpdate(5L)).thenReturn(Optional.of(slot));

        assertThrows(SlotUnavailableException.class, () -> bookingService.create(request));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createPersistsBookingAndLocksSlot() {
        when(slotRepository.findByIdForUpdate(5L)).thenReturn(Optional.of(slot));
        when(bookingRepository.existsBySlotIdAndStatusIn(anyLong(), any())).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));
        when(bookingMapper.toResponse(any())).thenReturn(null);

        bookingService.create(request);

        verify(slotRepository).save(slot);
        verify(bookingRepository).save(any(Booking.class));
    }
}

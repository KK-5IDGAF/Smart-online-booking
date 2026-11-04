package com.smartbooking.service;

import com.smartbooking.dto.booking.BookingRequest;
import com.smartbooking.dto.booking.BookingResponse;
import com.smartbooking.entity.Booking;
import com.smartbooking.entity.BookingStatus;
import com.smartbooking.entity.Slot;
import com.smartbooking.entity.User;
import com.smartbooking.exception.BookingNotFoundException;
import com.smartbooking.exception.ResourceNotFoundException;
import com.smartbooking.exception.SlotUnavailableException;
import com.smartbooking.exception.UnauthorizedException;
import com.smartbooking.mapper.BookingMapper;
import com.smartbooking.notification.NotificationChannel;
import com.smartbooking.notification.NotificationDispatcher;
import com.smartbooking.notification.NotificationMessage;
import com.smartbooking.repository.BookingRepository;
import com.smartbooking.repository.SlotRepository;
import com.smartbooking.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    private static final List<BookingStatus> ACTIVE_STATUSES = List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED);

    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;
    private final BookingMapper bookingMapper;
    private final SecurityUtils securityUtils;
    private final NotificationDispatcher notificationDispatcher;

    public BookingService(
            BookingRepository bookingRepository,
            SlotRepository slotRepository,
            BookingMapper bookingMapper,
            SecurityUtils securityUtils,
            NotificationDispatcher notificationDispatcher
    ) {
        this.bookingRepository = bookingRepository;
        this.slotRepository = slotRepository;
        this.bookingMapper = bookingMapper;
        this.securityUtils = securityUtils;
        this.notificationDispatcher = notificationDispatcher;
    }

    /**
     * Double-booking prevention: pessimistic lock on the slot row, then
     * availability + active-booking checks inside one transaction.
     */
    @Transactional
    public BookingResponse create(BookingRequest request) {
        User user = securityUtils.currentUser();
        Slot slot = slotRepository.findByIdForUpdate(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + request.getSlotId()));

        if (!slot.isAvailable() || bookingRepository.existsBySlotIdAndStatusIn(slot.getId(), ACTIVE_STATUSES)) {
            throw new SlotUnavailableException("Slot is not available");
        }

        Booking booking = Booking.builder()
                .user(user)
                .slot(slot)
                .status(BookingStatus.CONFIRMED)
                .build();
        bookingRepository.save(booking);
        slot.setAvailable(false);
        slotRepository.save(slot);

        notifyUser(user, "Booking confirmed",
                "Your booking for " + slot.getResource().getName() + " on " + slot.getSlotDate() + " is confirmed.");
        return bookingMapper.toResponse(booking);
    }

    public List<BookingResponse> myBookings() {
        return bookingRepository.findByUserIdWithDetails(securityUtils.currentUser().getId())
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    public List<BookingResponse> allBookings() {
        return bookingRepository.findAllWithDetails().stream().map(bookingMapper::toResponse).toList();
    }

    @Transactional
    public BookingResponse cancel(Long bookingId) {
        User current = securityUtils.currentUser();
        Booking booking = bookingRepository.findByIdWithDetails(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + bookingId));

        boolean owner = booking.getUser().getId().equals(current.getId());
        boolean privileged = current.getRole().name().equals("ADMIN") || current.getRole().name().equals("STAFF");
        if (!owner && !privileged) {
            throw new UnauthorizedException("You cannot cancel this booking");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Slot slot = booking.getSlot();
        slot.setAvailable(true);
        slotRepository.save(slot);
        bookingRepository.save(booking);

        notifyUser(booking.getUser(), "Booking cancelled",
                "Your booking for " + slot.getResource().getName() + " was cancelled.");
        return bookingMapper.toResponse(booking);
    }

    private void notifyUser(User user, String subject, String body) {
        NotificationMessage message = NotificationMessage.builder()
                .recipient(user)
                .subject(subject)
                .body(body)
                .build();
        notificationDispatcher.notifyChannel(NotificationChannel.DASHBOARD, message);
    }
}

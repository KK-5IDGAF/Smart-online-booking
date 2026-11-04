package com.smartbooking.mapper;

import com.smartbooking.dto.booking.BookingResponse;
import com.smartbooking.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .userName(booking.getUser().getName())
                .slotId(booking.getSlot().getId())
                .resourceId(booking.getSlot().getResource().getId())
                .resourceName(booking.getSlot().getResource().getName())
                .slotDate(booking.getSlot().getSlotDate())
                .startTime(booking.getSlot().getStartTime())
                .endTime(booking.getSlot().getEndTime())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}

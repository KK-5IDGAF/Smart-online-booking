package com.smartbooking.dto.booking;

import com.smartbooking.entity.BookingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class BookingResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long slotId;
    private Long resourceId;
    private String resourceName;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private BookingStatus status;
    private Instant createdAt;
}

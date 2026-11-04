package com.smartbooking.dto.slot;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
public class SlotResponse {
    private Long id;
    private Long resourceId;
    private String resourceName;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean available;
}

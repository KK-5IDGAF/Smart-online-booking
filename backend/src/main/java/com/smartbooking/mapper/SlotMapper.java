package com.smartbooking.mapper;

import com.smartbooking.dto.slot.SlotResponse;
import com.smartbooking.entity.Slot;
import org.springframework.stereotype.Component;

@Component
public class SlotMapper {

    public SlotResponse toResponse(Slot slot) {
        return SlotResponse.builder()
                .id(slot.getId())
                .resourceId(slot.getResource().getId())
                .resourceName(slot.getResource().getName())
                .slotDate(slot.getSlotDate())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .available(slot.isAvailable())
                .build();
    }
}

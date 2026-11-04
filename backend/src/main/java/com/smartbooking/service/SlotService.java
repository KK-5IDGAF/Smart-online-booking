package com.smartbooking.service;

import com.smartbooking.dto.slot.SlotRequest;
import com.smartbooking.dto.slot.SlotResponse;
import com.smartbooking.entity.Resource;
import com.smartbooking.entity.Slot;
import com.smartbooking.exception.ResourceNotFoundException;
import com.smartbooking.mapper.SlotMapper;
import com.smartbooking.repository.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SlotService {

    private final SlotRepository slotRepository;
    private final ResourceService resourceService;
    private final SlotMapper slotMapper;

    public SlotService(SlotRepository slotRepository, ResourceService resourceService, SlotMapper slotMapper) {
        this.slotRepository = slotRepository;
        this.resourceService = resourceService;
        this.slotMapper = slotMapper;
    }

    public List<SlotResponse> findAll(Long resourceId, LocalDate date) {
        List<Slot> slots;
        if (resourceId != null && date != null) {
            slots = slotRepository.findByResourceIdAndSlotDate(resourceId, date);
        } else if (resourceId != null) {
            slots = slotRepository.findByResourceId(resourceId);
        } else {
            slots = slotRepository.findAllWithResource();
        }
        return slots.stream().map(slotMapper::toResponse).toList();
    }

    public List<SlotResponse> findAvailable() {
        return slotRepository.findAvailableWithResource().stream().map(slotMapper::toResponse).toList();
    }

    @Transactional
    public SlotResponse create(SlotRequest request) {
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        Resource resource = resourceService.getResource(request.getResourceId());
        Slot slot = Slot.builder()
                .resource(resource)
                .slotDate(request.getSlotDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .available(request.getAvailable() == null || request.getAvailable())
                .build();
        return slotMapper.toResponse(slotRepository.save(slot));
    }

    @Transactional
    public SlotResponse update(Long id, SlotRequest request) {
        Slot slot = slotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + id));
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        Resource resource = resourceService.getResource(request.getResourceId());
        slot.setResource(resource);
        slot.setSlotDate(request.getSlotDate());
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        if (request.getAvailable() != null) {
            slot.setAvailable(request.getAvailable());
        }
        return slotMapper.toResponse(slotRepository.save(slot));
    }
}

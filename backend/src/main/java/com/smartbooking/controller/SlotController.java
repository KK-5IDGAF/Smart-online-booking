package com.smartbooking.controller;

import com.smartbooking.dto.slot.SlotRequest;
import com.smartbooking.dto.slot.SlotResponse;
import com.smartbooking.service.SlotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/slots")
@Tag(name = "Slots")
public class SlotController {

    private final SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping
    public ResponseEntity<List<SlotResponse>> list(
            @RequestParam(required = false) Long resourceId,
            @RequestParam(required = false) LocalDate date
    ) {
        return ResponseEntity.ok(slotService.findAll(resourceId, date));
    }

    @GetMapping("/available")
    public ResponseEntity<List<SlotResponse>> available() {
        return ResponseEntity.ok(slotService.findAvailable());
    }

    @PostMapping
    public ResponseEntity<SlotResponse> create(@Valid @RequestBody SlotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(slotService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SlotResponse> update(@PathVariable Long id, @Valid @RequestBody SlotRequest request) {
        return ResponseEntity.ok(slotService.update(id, request));
    }
}

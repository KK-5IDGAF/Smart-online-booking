package com.smartbooking.controller;

import com.smartbooking.dto.admin.DashboardStatsResponse;
import com.smartbooking.dto.booking.BookingResponse;
import com.smartbooking.dto.user.UserResponse;
import com.smartbooking.service.BookingService;
import com.smartbooking.service.ReportService;
import com.smartbooking.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin")
public class AdminController {

    private final ReportService reportService;
    private final BookingService bookingService;
    private final UserService userService;

    public AdminController(ReportService reportService, BookingService bookingService, UserService userService) {
        this.reportService = reportService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> dashboard() {
        return ResponseEntity.ok(reportService.dashboard());
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponse>> bookings() {
        return ResponseEntity.ok(bookingService.allBookings());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> users() {
        return ResponseEntity.ok(userService.findAll());
    }
}

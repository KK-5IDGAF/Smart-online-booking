package com.smartbooking.dto.admin;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DashboardStatsResponse {
    private long totalUsers;
    private long totalResources;
    private long totalSlots;
    private long totalBookings;
    private long activeBookings;
    private long cancelledBookings;
    private long pendingBookings;
    private long confirmedBookings;
}

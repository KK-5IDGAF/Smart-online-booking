package com.smartbooking.notification;

/**
 * Abstraction for outbound notifications. Concrete channels (dashboard, email, SMS)
 * implement this contract so BookingService depends on the interface, not a vendor.
 */
public interface NotificationService {

    NotificationChannel channel();

    void send(NotificationMessage message);

    default boolean supports(NotificationChannel requested) {
        return channel() == requested;
    }
}

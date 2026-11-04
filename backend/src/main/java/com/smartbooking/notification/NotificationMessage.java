package com.smartbooking.notification;

import com.smartbooking.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationMessage {
    private User recipient;
    private String subject;
    private String body;
}

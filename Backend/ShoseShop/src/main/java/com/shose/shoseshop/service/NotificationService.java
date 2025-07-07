package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.response.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(String userId, Notification notification) {
        log.info("Sending notification to {} with payload {}", userId, notification);
        simpMessagingTemplate.convertAndSendToUser(userId, "/notification", notification);
    }
}

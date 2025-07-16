package com.shose.shoseshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import com.shose.shoseshop.controller.request.Notice;
import com.shose.shoseshop.controller.response.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(List<String> msg, Long userId) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(msg);
            simpMessagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/notification",
                    msg
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendToUser(String userId, String title, String body, String fcmToken) throws FirebaseMessagingException {
        Map<String, String> data = new HashMap<>();
        data.put("userId", userId);

        Message message = Message.builder()
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .putAllData(data)
                .setToken(fcmToken)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Gửi thông báo thành công cho userId: " + userId + ", response: " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Lỗi khi gửi thông báo cho userId " + userId + ": " + e.getMessage());
            throw e;
        }
    }


}

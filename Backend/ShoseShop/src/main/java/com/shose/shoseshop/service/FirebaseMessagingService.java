package com.shose.shoseshop.service;

import com.google.firebase.messaging.*;
import com.shose.shoseshop.controller.request.Notice;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    public BatchResponse sendMulticastMessage(Notice notice) throws FirebaseMessagingException {
        MulticastMessage message = MulticastMessage.builder()
                .setNotification(Notification.builder()
                        .setTitle(notice.getSubject())
                        .setBody(notice.getContent())
                        .setImage(notice.getImage())
                        .build())
                .putAllData(notice.getData())
                .addAllTokens(notice.getRegistrationTokens())
                .build();

        return FirebaseMessaging.getInstance().sendMulticast(message);
    }
}

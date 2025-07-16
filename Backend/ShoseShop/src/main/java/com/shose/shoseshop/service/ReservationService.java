package com.shose.shoseshop.service;

import com.shose.shoseshop.constant.PageData;
import com.shose.shoseshop.constant.ReservationStatus;
import com.shose.shoseshop.controller.FCMController;
import com.shose.shoseshop.controller.request.ResFilter;
import com.shose.shoseshop.entity.Reservation;
import com.shose.shoseshop.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;
    private final FirebaseMessagingService firebaseMessagingService;
    private final FCMController fcmController;

    public PageData<Reservation> getRes(ResFilter request, Pageable pageable) {
        Page<Reservation> page = reservationRepository.getAllReservation(request.getStatuses(),
        request.getDateFrom(),
                request.getDateTo(),
                request.getTimeFrom(),
                request.getTimeTo(),
                pageable);
        return new PageData<>(
                page.getContent(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Scheduled(fixedRate = 60000)
    public void updateStatus() {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            reservation.setStatus(ReservationStatus.RESERVED);
            notificationService.sendNotification(List.of("Send notification success"), reservation.getUserId());
        }
        reservationRepository.saveAll(reservations);
    }

    public void updateStatusToPending() {
        List<Reservation> reservations = reservationRepository.findAll();
        for (Reservation reservation : reservations) {
            reservation.setStatus(ReservationStatus.RESERVED);
            notificationService.sendNotification(List.of("update to pending success"), reservation.getUserId());
        }
        reservationRepository.saveAll(reservations);
    }
}

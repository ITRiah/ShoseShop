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

import java.util.ArrayList;
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

    @Scheduled(fixedRate = 10000)
    public void updateStatus() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<Reservation> updatedReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            reservation.setStatus(ReservationStatus.RESERVED);
            updatedReservations.add(reservation);

            String userId = String.valueOf(reservation.getUserId());
            String fcmToken = fcmController.getToken(userId);

            if (fcmToken != null && !fcmToken.isEmpty()) {
                try {
                    notificationService.sendToUser(
                            userId,
                            "Cập nhật đặt chỗ #" + reservation.getId(),
                            "Đặt chỗ của bạn đã được cập nhật thành: " + reservation.getStatus(),
                            fcmToken
                    );
                    System.out.println("Gửi thông báo thành công cho userId: " + userId);
                } catch (Exception e) {
                    System.err.println("Lỗi khi gửi thông báo cho userId " + userId + ": " + e.getMessage());
                }
            } else {
                System.out.println("Không tìm thấy fcmToken cho userId: " + userId);
            }
        }

        reservationRepository.saveAll(updatedReservations);
    }
}

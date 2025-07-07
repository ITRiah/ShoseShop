package com.shose.shoseshop.service;

import com.shose.shoseshop.constant.PageData;
import com.shose.shoseshop.constant.ReservationStatus;
import com.shose.shoseshop.controller.request.ResFilter;
import com.shose.shoseshop.controller.response.Notification;
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
        reservations.forEach(i -> i.setStatus(ReservationStatus.RESERVED));
        reservations.forEach(reservation -> {
            notificationService.sendNotification(reservation.getUserId().toString(), new Notification(ReservationStatus.RESERVED, "Update status reserved successful!"));
        });
        reservationRepository.saveAll(reservations);
    }
}

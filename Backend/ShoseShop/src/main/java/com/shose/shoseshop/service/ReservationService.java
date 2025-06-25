package com.shose.shoseshop.service;

import com.shose.shoseshop.constant.PageData;
import com.shose.shoseshop.controller.request.ResFilter;
import com.shose.shoseshop.entity.Reservation;
import com.shose.shoseshop.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

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
}

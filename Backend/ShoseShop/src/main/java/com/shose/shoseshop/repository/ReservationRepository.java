package com.shose.shoseshop.repository;

import com.shose.shoseshop.constant.ReservationStatus;
import com.shose.shoseshop.controller.request.ResFilter;
import com.shose.shoseshop.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Query("""
                SELECT r FROM Reservation r
                WHERE (:statuses IS NULL OR r.status IN :statuses)
                  AND CAST(r.startTime AS date) >= COALESCE(:dateFrom, CAST(r.startTime AS date))
                  AND CAST(r.startTime AS date) <= COALESCE(:dateTo, CAST(r.startTime AS date))
                  AND CAST(r.startTime AS time) >= COALESCE(:timeFrom, CAST(r.startTime AS time))
                  AND CAST(r.endTime AS time) <= COALESCE(:timeTo, CAST(r.endTime AS time))
            """)
    Page<Reservation> getAllReservation(
            @Param("statuses") Set<ReservationStatus> statuses,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("timeFrom") LocalTime timeFrom,
            @Param("timeTo") LocalTime timeTo,
            Pageable pageable
    );
}

package com.shose.shoseshop.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shose.shoseshop.constant.ReservationStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
public class ResFilter {
    private Set<ReservationStatus> statuses;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateFrom;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateTo;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeFrom;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime timeTo;

}

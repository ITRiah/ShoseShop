package com.shose.shoseshop.controller.response;

import com.shose.shoseshop.constant.ReservationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    private ReservationStatus status;
    private String message;
}

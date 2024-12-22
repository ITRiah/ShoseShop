package com.shose.shoseshop.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

public abstract class PaymentDTO {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}
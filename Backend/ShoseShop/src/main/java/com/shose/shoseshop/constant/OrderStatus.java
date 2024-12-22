package com.shose.shoseshop.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("Chờ xử lý"),
    CONFIRMED("Xác nhận"),
    PROCESSING("Đang xử lý"),
    SHIPPED("Đang vận chuyển"),
    DELIVERED("Đã giao"),
    CANCELED("Hủy");

    private final String value;

    // Getter method để lấy mô tả
    public String getValue() {
        return value;
    }
}

package com.shose.shoseshop.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING,        // Đơn hàng đang chờ xử lý
    CONFIRMED,      // Đơn hàng đã được xác nhận
    PROCESSING,     // Đơn hàng đang được xử lý (đóng gói, chuẩn bị giao)
    SHIPPED,        // Đơn hàng đã được giao cho đơn vị vận chuyển
    DELIVERED,      // Đơn hàng đã được giao đến khách hàng
    CANCELED;       // Đơn hàng đã bị hủy bởi người dùng hoặc hệ thống
}

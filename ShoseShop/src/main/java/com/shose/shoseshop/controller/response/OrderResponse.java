package com.shose.shoseshop.controller.response;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.constant.PaymentMethod;
import com.shose.shoseshop.constant.ShippingMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String fullName;
    private String phone;
    private OrderStatus status;
    private String reason;
    private Date orderDate;
    private String shippingAddress;
    private ShippingMethod shippingMethod;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private String note;
}
package com.shose.shoseshop.controller.response;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.constant.PaymentMethod;
import com.shose.shoseshop.constant.PaymentStatus;
import com.shose.shoseshop.constant.ShippingMethod;
import com.shose.shoseshop.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse extends BaseEntity {
    private Long id;
    private String fullName;
    private String phone;
    private OrderStatus status;
    private String reason;
    private String shippingAddress;
    private ShippingMethod shippingMethod;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private String note;
    private PaymentStatus paymentStatus;
    private List<OrderDetailResponse> orderDetails;
    private Long voucherId;
}

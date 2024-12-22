package com.shose.shoseshop.controller.request;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.constant.PaymentMethod;
import com.shose.shoseshop.constant.PaymentStatus;
import com.shose.shoseshop.constant.ShippingMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String fullName;
    private String phone;
    private String reason;
    private String shippingAddress;
    private ShippingMethod shippingMethod;
    private PaymentMethod paymentMethod;
    private String note;
    private PaymentStatus paymentStatus;
    private Set<Long> cartDetailIds;
    private Long voucherId;
}

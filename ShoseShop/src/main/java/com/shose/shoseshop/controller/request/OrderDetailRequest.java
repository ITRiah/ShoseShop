package com.shose.shoseshop.controller.request;

import com.shose.shoseshop.entity.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequest {
    private Long orderId;
    private ProductDetail productDetailId;
    private Integer quantity;
}

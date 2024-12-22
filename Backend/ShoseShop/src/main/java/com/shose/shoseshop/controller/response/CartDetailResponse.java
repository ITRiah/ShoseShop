package com.shose.shoseshop.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailResponse {
    private Long id;
    private String productName;
    private ProductDetailResponse productDetail;
    private Long quantity;
    private Long cartId;
}

package com.shose.shoseshop.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shose.shoseshop.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse extends BaseEntity {
    private Long id;
    private ProductDetailResponse productDetail;
    private String productName;
    private Long quantity;
}

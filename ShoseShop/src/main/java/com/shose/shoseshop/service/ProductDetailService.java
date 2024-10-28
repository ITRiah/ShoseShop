package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.ProductDetailRequest;
import com.shose.shoseshop.controller.response.ProductDetailResponse;

import java.util.List;

public interface ProductDetailService {
    void create(ProductDetailRequest productDetailRequest);

    List<ProductDetailResponse> getByProductId(Long productId);
}

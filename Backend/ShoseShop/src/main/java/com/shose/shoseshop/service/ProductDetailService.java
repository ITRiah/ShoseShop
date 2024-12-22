package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.ProductDetailRequest;
import com.shose.shoseshop.controller.response.ProductDetailResponse;

import java.io.IOException;
import java.util.List;

public interface ProductDetailService {
    void create(ProductDetailRequest productDetailRequest);

    List<ProductDetailResponse> getByProductId(Long productId);

    void update(ProductDetailRequest productDetailRequest);

    void delete(Long id);

    ProductDetailResponse getById(Long id);
}

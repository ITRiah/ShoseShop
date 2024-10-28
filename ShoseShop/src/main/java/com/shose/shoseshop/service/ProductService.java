package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ProductResponse;

import java.util.List;

public interface ProductService {
    void create(ProductRequest productRequest);

    List<ProductResponse> getByCategory(Long categoryId);
}

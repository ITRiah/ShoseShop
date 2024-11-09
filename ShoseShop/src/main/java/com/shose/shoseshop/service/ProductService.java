package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ProductResponse;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    String create(ProductRequest productRequest) throws IOException;

    List<ProductResponse> getByCategory(Long categoryId);
}

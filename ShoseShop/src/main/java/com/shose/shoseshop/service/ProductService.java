package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    void create(ProductRequest productRequest);

    List<ProductResponse> getByCategory(Long categoryId);

    Page<ProductResponse> listProduct(Pageable pageable, ProductFilterRequest request);

    void delete(Long id);

    void update(ProductRequest productRequest);

    ProductResponse getById(Long id);
}

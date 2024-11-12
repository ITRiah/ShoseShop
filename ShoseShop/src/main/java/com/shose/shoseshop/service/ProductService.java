package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    String create(ProductRequest productRequest) throws IOException;

    List<ProductResponse> getByCategory(Long categoryId);

    Page<ProductResponse> listProduct(Pageable pageable, ProductFilterRequest request);
}

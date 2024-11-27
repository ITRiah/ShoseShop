package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.CategoryRequest;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    void create(CategoryRequest categoryRequest);

    Page<CategoryResponse> getAll(Pageable pageable, OrderFilterRequest request);

    void update(CategoryRequest categoryRequest);

    void delete(Long id);

    CategoryResponse getById(Long id);
}

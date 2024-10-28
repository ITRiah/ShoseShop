package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.CategoryRequest;
import com.shose.shoseshop.controller.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    void create(CategoryRequest categoryRequest);

    List<CategoryResponse> getAll();
}

package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.CategoryRequest;
import com.shose.shoseshop.controller.response.CategoryResponse;
import com.shose.shoseshop.entity.Category;
import com.shose.shoseshop.repository.CategoryRepository;
import com.shose.shoseshop.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    @Override
    public void create(CategoryRequest categoryRequest) {
        categoryRepository.save(new Category(categoryRequest.getName()));
    }

    @Override
    public List<CategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        return categories.stream()
                .map(category -> new ModelMapper().map(category, CategoryResponse.class))
                .collect(Collectors.toList());
    }
}

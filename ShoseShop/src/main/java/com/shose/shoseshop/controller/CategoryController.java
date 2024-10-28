package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.CategoryRequest;
import com.shose.shoseshop.controller.response.CategoryResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    public ResponseData<Void> create(@Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.create(categoryRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Create procedure is success!");
    }

    @GetMapping
    public ResponseData<List<CategoryResponse>> getAll() {
        return new ResponseData<>(categoryService.getAll());
    }
}

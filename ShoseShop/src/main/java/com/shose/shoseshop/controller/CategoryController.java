package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.CategoryRequest;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.response.CategoryResponse;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.entity.Category_;
import com.shose.shoseshop.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    public ResponseData<Void> create(@Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.create(categoryRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Create category is success!");
    }

    @PutMapping
    public ResponseData<Void> update(@Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.update(categoryRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Update category is success!");
    }

    @GetMapping
    public ResponseData<CategoryResponse> getAll(@PageableDefault(size = 10)
                                                 @SortDefault.SortDefaults({@SortDefault(sort = Category_.NAME, direction = Sort.Direction.DESC)})
                                                 Pageable pageable,
                                                 @RequestBody(required = false) OrderFilterRequest request) {
        return new ResponseData<>(categoryService.getAll(pageable, request));
    }

    @DeleteMapping
    public ResponseData<Void> delete(@RequestParam Long id) {
        categoryService.delete(id);
        return new ResponseData<>(HttpStatus.CREATED, "Delete category is success!");
    }

    @GetMapping("/{id}")
    public ResponseData<CategoryResponse> getById(@PathVariable Long id) {
        return new ResponseData<>(categoryService.getById(id));
    }
}

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
import org.springframework.data.domain.PageRequest;
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
        return new ResponseData<>(HttpStatus.CREATED, "Tạo danh mục thành công!");
    }

    @PutMapping
    public ResponseData<Void> update(@Valid @RequestBody CategoryRequest categoryRequest) {
        categoryService.update(categoryRequest);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Cập nhật danh mục thành công!");
    }

    @PostMapping("/search")
    public ResponseData<CategoryResponse> getAll(@PageableDefault(size = 10)
                                                 @SortDefault.SortDefaults({@SortDefault(sort = Category_.NAME, direction = Sort.Direction.DESC)})
                                                 Pageable pageable,
                                                 @RequestBody(required = false) OrderFilterRequest request) {
        int page = (request != null && request.getPage() != null) ? request.getPage() : pageable.getPageNumber();
        int size = (request != null && request.getSize() != null) ? request.getSize() : pageable.getPageSize();
        Pageable customPageable = (page == pageable.getPageNumber() && size == pageable.getPageSize())
                ? pageable
                : PageRequest.of(page, size, pageable.getSort());
        return new ResponseData<>(categoryService.getAll(customPageable, request));
    }

    @DeleteMapping
    public ResponseData<Void> delete(@RequestParam Long id) {
        categoryService.delete(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Danh mục được đã xóa!");
    }

    @GetMapping("/{id}")
    public ResponseData<CategoryResponse> getById(@PathVariable Long id) {
        return new ResponseData<>(categoryService.getById(id));
    }
}

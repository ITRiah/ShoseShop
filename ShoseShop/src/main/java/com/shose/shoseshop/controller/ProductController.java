package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.entity.Product_;
import com.shose.shoseshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping
    public ResponseData<Void> create(@RequestBody ProductRequest productRequest) {
        productService.create(productRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Sản phẩm đã được thêm mới thành công!");
    }

    @PostMapping("/search")
    public ResponseData<ProductResponse> getAll(@PageableDefault(size = 10)
                                       @SortDefault.SortDefaults({@SortDefault(sort = Product_.NAME, direction = Sort.Direction.ASC)})
                                       Pageable pageable,
                                       @RequestBody(required = false) ProductFilterRequest request) {
        int page = (request != null && request.getPage() != null) ? request.getPage() : pageable.getPageNumber();
        int size = (request != null && request.getSize() != null) ? request.getSize() : pageable.getPageSize();
        Pageable customPageable = (page == pageable.getPageNumber() && size == pageable.getPageSize())
                ? pageable
                : PageRequest.of(page, size, pageable.getSort());
        return new ResponseData<>(productService.listProduct(customPageable, request));
    }

    @DeleteMapping
    public ResponseData<Void> delete(@RequestParam Long id) throws IOException {
        productService.delete(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Sản phẩm đã bị xóa bỏ!");
    }

    @PutMapping
    public ResponseData<Void> update(@RequestBody ProductRequest productRequest) {
        productService.update(productRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Cập nhật sản phẩm thành công!");
    }

    @GetMapping("/{id}")
    public ResponseData<ProductResponse> getById(@PathVariable Long id) {
        return new ResponseData<>(productService.getById(id));
    }
}


package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.entity.Product_;
import com.shose.shoseshop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseData<Void> create(@RequestBody ProductRequest productRequest) throws IOException {
        productService.create(productRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Create product success!");
    }

    @GetMapping("/{categoryId}")
    public ResponseData<List<ProductResponse>> getProductByCategory(@PathVariable(value = "categoryId") Long categoryId) {
        return new ResponseData<>(productService.getByCategory(categoryId));
    }

    @GetMapping
    public ResponseData<ProductResponse> getAll(@PageableDefault(size = 10)
                                       @SortDefault.SortDefaults({@SortDefault(sort = Product_.NAME, direction = Sort.Direction.ASC)})
                                       Pageable pageable,
                                       @RequestBody(required = false) ProductFilterRequest request) {
        return new ResponseData<>(productService.listProduct(pageable, request));
    }

    @DeleteMapping
    public ResponseData<Void> create(@RequestParam Long id) throws IOException {
        productService.delete(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Delete product success!");
    }

    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseData<Void> update(@RequestBody ProductRequest productRequest) throws IOException {
        productService.create(productRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Update product success!");
    }

    @GetMapping("/{id}")
    public ResponseData<ProductResponse> getById(@PathVariable Long id) {
        return new ResponseData<>(productService.getById(id));
    }
}


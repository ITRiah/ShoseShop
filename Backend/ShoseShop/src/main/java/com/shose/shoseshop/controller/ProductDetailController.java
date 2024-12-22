package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ProductDetailRequest;
import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ProductDetailResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.controller.response.VoucherResponse;
import com.shose.shoseshop.service.ProductDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/product-details")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProductDetailController {

    ProductDetailService productDetailService;

    @PostMapping
    public ResponseData<String> create(@RequestBody ProductDetailRequest productDetailRequest) {
        productDetailService.create(productDetailRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Thêm chi tiết cho sản pẩm thành công!");
    }

    @PutMapping
    public ResponseData<String> update(@RequestBody ProductDetailRequest productDetailRequest) {
        productDetailService.update(productDetailRequest);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Cập nhật chi tiết sản phẩm thành công!");
    }

    @DeleteMapping
    public ResponseData<String> delete(@RequestParam Long id) {
        productDetailService.delete(id);
        return new ResponseData<>(HttpStatus.CREATED, "Chi tiết sản phẩm đã được xóa bỏ!");
    }

    @GetMapping("/{id}")
    public ResponseData<ProductDetailResponse> getById(@PathVariable Long id) {
        return new ResponseData<>(productDetailService.getById(id));
    }
}

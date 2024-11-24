package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ProductDetailRequest;
import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ResponseData;
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

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseData<String> create(@Valid @ModelAttribute ProductDetailRequest productDetailRequest) throws IOException {
        productDetailService.create(productDetailRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Create product detail success!");
    }

    @PutMapping
    public ResponseData<String> update(@Valid @ModelAttribute ProductDetailRequest productDetailRequest) throws IOException {
        productDetailService.update(productDetailRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Update product detail success!");
    }
}

package com.shose.shoseshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product/details")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class ProductDetailController {

}

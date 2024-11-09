package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @Value("${upload.folder.product}")
    String UPLOAD_FOLDER;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseData<String> create(@Valid @ModelAttribute ProductRequest productRequest) throws IOException {
        return new ResponseData<>(HttpStatus.CREATED,  productService.create(productRequest));
    }

    @GetMapping("/{categoryId}")
    public ResponseData<List<ProductResponse>> getProductByCategory(@PathVariable(value = "categoryId") Long categoryId) {
        return new ResponseData<>(productService.getByCategory(categoryId));
    }

    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
        File file = new File(UPLOAD_FOLDER + fileName);
        Files.copy(file.toPath(), response.getOutputStream());
    }
}

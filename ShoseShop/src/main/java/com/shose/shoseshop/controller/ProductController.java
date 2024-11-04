package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ResourceLoader resourceLoader;

    @Value("${upload.folder.product}")
    String UPLOAD_FOLDER;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseData<Void> create(@Valid @ModelAttribute ProductRequest productRequest) throws IOException {
        if (!(new File(UPLOAD_FOLDER).exists())) {
            new File(UPLOAD_FOLDER).mkdirs();
        }
        MultipartFile file = productRequest.getFile();
        if (file != null) {
            String fileName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID() + "_" + fileName;
            String filePath = UPLOAD_FOLDER + uniqueFileName;
            file.transferTo(new File(filePath));
            productRequest.setImg(uniqueFileName);
        }
        productService.create(productRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Create product is success!");
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

    @GetMapping("/image/{fileName:.+}")
    public ResponseData<String> getImageBase64(@PathVariable("fileName") String fileName) throws IOException {
        File file = new File(UPLOAD_FOLDER + fileName);
        if (!file.exists()) {
            return new ResponseData<>(HttpStatus.NOT_FOUND, "File not found", null);
        }
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        return new ResponseData<>(encodedString);
    }
}

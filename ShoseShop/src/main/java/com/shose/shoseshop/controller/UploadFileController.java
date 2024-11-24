package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.request.UserRequest;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.UploadImageService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UploadFileController {

    UploadImageService uploadImageService;
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseData<String> create(MultipartFile file) throws IOException {
        return new ResponseData<>(HttpStatus.CREATED, "Upload image success!", uploadImageService.uploadImage(file));
    }
}

package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.VoucherRequest;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.VoucherService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vouchers")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VoucherController {
    VoucherService voucherService;

    @PostMapping
    public ResponseData<Void> create(@Valid @RequestBody VoucherRequest voucherRequest) {
        voucherService.create(voucherRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Create voucher is success!");
    }
}

package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.VoucherRequest;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.controller.response.VoucherResponse;
import com.shose.shoseshop.service.VoucherService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseData<List<VoucherResponse>> getAll() {
        voucherService.getAll();
        return new ResponseData<>(HttpStatus.CREATED, "Create voucher is success!");
    }
}

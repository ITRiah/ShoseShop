package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.VoucherRequest;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.controller.response.VoucherResponse;
import com.shose.shoseshop.entity.User_;
import com.shose.shoseshop.entity.Voucher_;
import com.shose.shoseshop.service.VoucherService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
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

    @PutMapping
    public ResponseData<Void> update(@Valid @RequestBody VoucherRequest voucherRequest) {
        voucherService.create(voucherRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Update voucher is success!");
    }

    @DeleteMapping
    public ResponseData<Void> delete(@RequestParam Long id) {
        voucherService.delete(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Delete voucher is success!");
    }

    @GetMapping("/user")
    public ResponseData<List<VoucherResponse>> getAllForUser() {
        return new ResponseData<>(HttpStatus.CREATED, "Success!", voucherService.getAllForUser());
    }

    @GetMapping("/admin")
    public ResponseData<VoucherResponse> getAllForAdmin(@PageableDefault(size = 10)
                                                        @SortDefault.SortDefaults({@SortDefault(sort = Voucher_.CREATED_AT, direction = Sort.Direction.DESC)})
                                                        Pageable pageable,
                                                        @RequestBody(required = false) OrderFilterRequest request) {
        return new ResponseData<>(voucherService.getAllForAdmin(pageable, request));
    }
}

package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.VoucherRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
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
import org.springframework.data.domain.PageRequest;
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
        return new ResponseData<>(HttpStatus.CREATED, "Voucher đã được thêm mới!");
    }

    @PutMapping
    public ResponseData<Void> update(@Valid @RequestBody VoucherRequest voucherRequest) {
        voucherService.update(voucherRequest);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Cập nhật voucher thành công!");
    }

    @DeleteMapping
    public ResponseData<Void> delete(@RequestParam Long id) {
        voucherService.delete(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Voucher đã bị xóa bỏ!");
    }

    @PostMapping("/search")
    public ResponseData<VoucherResponse> getAllForAdmin(@PageableDefault(size = 10)
                                                        @SortDefault.SortDefaults({@SortDefault(sort = Voucher_.CREATED_AT, direction = Sort.Direction.DESC)})
                                                        Pageable pageable,
                                                        @RequestBody(required = false) OrderFilterRequest request) {
        int page = (request != null && request.getPage() != null) ? request.getPage() : pageable.getPageNumber();
        int size = (request != null && request.getSize() != null) ? request.getSize() : pageable.getPageSize();
        Pageable customPageable = (page == pageable.getPageNumber() && size == pageable.getPageSize())
                ? pageable
                : PageRequest.of(page, size, pageable.getSort());
        return new ResponseData<>(voucherService.getAllForAdmin(customPageable, request));
    }

    @GetMapping("/{id}")
    public ResponseData<VoucherResponse> getById(@PathVariable Long id) {
        return new ResponseData<>(voucherService.getById(id));
    }
}

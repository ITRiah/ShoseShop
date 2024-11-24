package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.VoucherRequest;
import com.shose.shoseshop.controller.response.ProductDetailResponse;
import com.shose.shoseshop.controller.response.VoucherResponse;
import com.shose.shoseshop.entity.ProductDetail;
import com.shose.shoseshop.entity.Voucher;
import com.shose.shoseshop.repository.VoucherRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VoucherService {
    VoucherRepository voucherRepository;

    public void create(VoucherRequest voucherRequest) {
        voucherRepository.save(new ModelMapper().map(voucherRequest, Voucher.class));
    }

    public List<VoucherResponse> getAll() {
        List<Voucher> vouchers = voucherRepository.findAll();
        return vouchers.stream()
                .map(voucher -> new ModelMapper().map(voucher, VoucherResponse.class))
                .collect(Collectors.toList());
    }
}

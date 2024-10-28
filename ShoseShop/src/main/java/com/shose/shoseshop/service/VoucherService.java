package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.VoucherRequest;
import com.shose.shoseshop.entity.Voucher;
import com.shose.shoseshop.repository.VoucherRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VoucherService {
    VoucherRepository voucherRepository;

    public void create(VoucherRequest voucherRequest) {
        voucherRepository.save(new ModelMapper().map(voucherRequest, Voucher.class));
    }

}

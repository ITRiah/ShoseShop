package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.VoucherRequest;
import com.shose.shoseshop.controller.response.VoucherResponse;
import com.shose.shoseshop.entity.Voucher;
import com.shose.shoseshop.repository.VoucherRepository;
import com.shose.shoseshop.specification.VoucherSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class VoucherService {
    VoucherRepository voucherRepository;
    ModelMapper modelMapper;

    @Transactional
    public void create(VoucherRequest voucherRequest) {
        voucherRepository.save(modelMapper.map(voucherRequest, Voucher.class));
    }

    public Page<VoucherResponse> getAllForAdmin(Pageable pageable, OrderFilterRequest request) {
        Specification<Voucher> spec = VoucherSpecification.generateFilter(request);
        Page<Voucher> voucherPage = voucherRepository.findAll(spec, pageable);
        return voucherPage.map(voucher -> modelMapper.map(voucher, VoucherResponse.class));
    }

    public List<VoucherResponse> getAllForUser() {
        List<Voucher> vouchers = voucherRepository.findByIsDeletedFalse();
        return vouchers.stream()
                .map(voucher -> modelMapper.map(voucher, VoucherResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(VoucherRequest voucherRequest) {
        Voucher voucher = voucherRepository.findById(voucherRequest.getId()).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(voucherRequest, voucher);
        voucherRepository.save(voucher);
    }

    @Transactional
    public void delete(Long id) {
        Voucher voucher = voucherRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        voucher.markAsDelete();
        voucherRepository.save(voucher);
    }
}

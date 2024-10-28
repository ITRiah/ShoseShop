package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.ProductDetailRequest;
import com.shose.shoseshop.controller.response.ProductDetailResponse;
import com.shose.shoseshop.entity.ProductDetail;
import com.shose.shoseshop.repository.ProductDetailRepository;
import com.shose.shoseshop.service.ProductDetailService;
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
public class ProductDetailImpl implements ProductDetailService {

    ProductDetailRepository productDetailRepository;

    @Override
    public void create(ProductDetailRequest productDetailRequest) {
        productDetailRepository.save(new ModelMapper().map(productDetailRequest, ProductDetail.class));
    }

    @Override
    public List<ProductDetailResponse> getByProductId(Long productId) {
        List<ProductDetail> productDetails = productDetailRepository.findAllByProductId(productId);
        return productDetails.stream()
                .map(productDetail -> new ModelMapper().map(productDetail, ProductDetailResponse.class))
                .collect(Collectors.toList());
    }
}

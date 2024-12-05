package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.ProductDetailRequest;
import com.shose.shoseshop.controller.response.ProductDetailResponse;
import com.shose.shoseshop.entity.ProductDetail;
import com.shose.shoseshop.repository.ProductDetailRepository;
import com.shose.shoseshop.service.ProductDetailService;
import com.shose.shoseshop.service.UploadImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductDetailImpl implements ProductDetailService {

    ProductDetailRepository productDetailRepository;
    UploadImageService uploadImageService;
    ProductDetailRepository getProductDetailRepository;
    ModelMapper modelMapper;

    @Override
    public void create(ProductDetailRequest productDetailRequest) {
        ProductDetail productDetail = new ModelMapper().map(productDetailRequest, ProductDetail.class);
        productDetail.setImg(productDetailRequest.getImg());
        productDetailRepository.save(productDetail);
    }

    @Override
    public List<ProductDetailResponse> getByProductId(Long productId) {
        List<ProductDetail> productDetails = productDetailRepository.findAllByProductId(productId);
        return productDetails.stream()
                .map(productDetail -> new ModelMapper().map(productDetail, ProductDetailResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void update(ProductDetailRequest productDetailRequest) {
        ProductDetail productDetail = productDetailRepository.findById(productDetailRequest.getId()).orElseThrow(EntityNotFoundException::new);
        productDetail.setColor(productDetailRequest.getColor());
        productDetail.setImg(productDetailRequest.getImg());
        productDetail.setSize(productDetailRequest.getSize());
        productDetail.setQuantity(productDetailRequest.getQuantity());
        productDetail.setPrice(productDetailRequest.getPrice());
        productDetailRepository.save(productDetail);
    }

    @Override
    public void delete(Long id) {
        ProductDetail productDetail = productDetailRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        productDetail.markAsDelete();
        productDetailRepository.save(productDetail);
    }

    @Override
    public ProductDetailResponse getById(Long id) {
        ProductDetail productDetail = productDetailRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(productDetail, ProductDetailResponse.class);
    }
}

package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.CategoryResponse;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.entity.*;
import com.shose.shoseshop.repository.CategoryRepository;
import com.shose.shoseshop.repository.ProcedureRepository;
import com.shose.shoseshop.repository.ProductDetailRepository;
import com.shose.shoseshop.repository.ProductRepository;
import com.shose.shoseshop.service.ProductService;
import com.shose.shoseshop.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProcedureRepository procedureRepository;
    CategoryRepository categoryRepository;
    UploadFileServiceImpl uploadFileService;
    ModelMapper modelMapper;
    ProductDetailRepository productDetailRepository;

    @Override
    @Transactional
    public void create(ProductRequest productRequest) throws IOException {
        Product product = new ModelMapper().map(productRequest, Product.class);
        Procedure procedure = procedureRepository.findById(productRequest.getProcedure())
                .orElseThrow(EntityNotFoundException::new);
        Category category = categoryRepository.findById(productRequest.getCategory())
                .orElseThrow(EntityNotFoundException::new);
        product.setProcedure(procedure);
        product.setCategory(category);
        String urlImage = uploadFileService.uploadImage(productRequest.getFile());
        product.setImg(urlImage);
        productRepository.save(product);
    }

    @Override
    public List<ProductResponse> getByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(product -> new ModelMapper().map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductResponse> listProduct(Pageable pageable, ProductFilterRequest request) {
        Specification<Product> specUser = ProductSpecification.generateFilterProducts(request);
        Page<Product> productPage = productRepository.findAll(specUser, pageable);
        return productPage.map(product -> modelMapper.map(product, ProductResponse.class));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<ProductDetail> productDetailList = product.getProductDetailResponseList();
        productDetailList.forEach(BaseEntity::markAsDelete);
        product.markAsDelete();
        productDetailRepository.saveAll(productDetailList);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void update(ProductRequest productRequest) throws IOException {
        Product product = productRepository.findById(productRequest.getId()).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(productRequest, Product.class);
        String urlImage = uploadFileService.uploadImage(productRequest.getFile());
        product.setImg(urlImage);
        productRepository.save(product);
    }
}
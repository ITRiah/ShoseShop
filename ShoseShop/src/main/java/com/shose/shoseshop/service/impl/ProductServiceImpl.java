package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.ProductRequest;
import com.shose.shoseshop.controller.response.CategoryResponse;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.entity.Category;
import com.shose.shoseshop.entity.Procedure;
import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.repository.CategoryRepository;
import com.shose.shoseshop.repository.ProcedureRepository;
import com.shose.shoseshop.repository.ProductRepository;
import com.shose.shoseshop.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
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
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProcedureRepository procedureRepository;
    CategoryRepository categoryRepository;

    @Override
    public void create(ProductRequest productRequest) {
        Product product = new ModelMapper().map(productRequest, Product.class);
        Procedure procedure = procedureRepository.findById(productRequest.getProcedure())
                .orElseThrow(EntityNotFoundException::new);
        Category category = categoryRepository.findById(productRequest.getCategory())
                .orElseThrow(EntityNotFoundException::new);
        product.setProcedure(procedure);
        product.setCategory(category);
        productRepository.save(product);
    }

    @Override
    public List<ProductResponse> getByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(product -> new ModelMapper().map(product, ProductResponse.class))
                .collect(Collectors.toList());
    }

}
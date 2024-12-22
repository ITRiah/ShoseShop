package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.CategoryRequest;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.response.CategoryResponse;
import com.shose.shoseshop.entity.BaseEntity;
import com.shose.shoseshop.entity.Category;
import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.entity.ProductDetail;
import com.shose.shoseshop.repository.CategoryRepository;
import com.shose.shoseshop.repository.ProductDetailRepository;
import com.shose.shoseshop.repository.ProductRepository;
import com.shose.shoseshop.service.CategoryService;
import com.shose.shoseshop.service.ProductService;
import com.shose.shoseshop.specification.CategorySpecification;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    ProductRepository productRepository;
    ProductDetailRepository productDetailRepository;
    ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(CategoryRequest categoryRequest) {
        categoryRepository.save(modelMapper.map(categoryRequest, Category.class));
    }

    @Override
    public Page<CategoryResponse> getAll(Pageable pageable, OrderFilterRequest request) {
        Specification<Category> spec = CategorySpecification.generateFilter(request);
        Page<Category> categoryPage = categoryRepository.findAll(spec, pageable);
        return categoryPage.map(category -> modelMapper.map(category, CategoryResponse.class));
    }

    @Override
    @Transactional
    public void update(CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryRequest.getId()).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(categoryRequest, category);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<Product> products = productRepository.findByCategoryId(id);
        List<ProductDetail> productDetailList = products.stream()
                .flatMap(product -> product.getProductDetailResponseList().stream())
                .toList();
        productDetailList.forEach(BaseEntity::markAsDelete);
        products.forEach(BaseEntity::markAsDelete);
        category.markAsDelete();
        productDetailRepository.saveAll(productDetailList);
        productRepository.saveAll(products);
        categoryRepository.save(category);
    }

    @Override
    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(category, CategoryResponse.class);
    }
}

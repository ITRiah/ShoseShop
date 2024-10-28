package com.shose.shoseshop.repository;

import com.shose.shoseshop.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long>, JpaSpecificationExecutor<ProductDetail> {

    List<ProductDetail> findAllByProductId(Long productId);
}

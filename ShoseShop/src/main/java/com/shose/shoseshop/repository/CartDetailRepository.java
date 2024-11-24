package com.shose.shoseshop.repository;

import com.shose.shoseshop.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long>, JpaSpecificationExecutor<CartDetail> {
    List<CartDetail> findByIdIn(@Param("cartDetailIds") Set<Long> ids);
}

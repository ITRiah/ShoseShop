package com.shose.shoseshop.repository;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.response.ProductStatisticResponse;
import com.shose.shoseshop.controller.response.StatisticResponse;
import com.shose.shoseshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Query("SELECT new com.shose.shoseshop.controller.response.StatisticResponse(" +
            "YEAR(o.createdAt), " +
            "MONTH(o.createdAt), " +
            "SUM(o.totalAmount)) " +
            "FROM Order o " +
            "WHERE o.status = :status " +
            "AND YEAR(o.createdAt) = :year " +
            "GROUP BY YEAR(o.createdAt), MONTH(o.createdAt) " +
            "ORDER BY YEAR(o.createdAt) DESC, MONTH(o.createdAt) DESC")
    List<StatisticResponse> findMonthlyRevenue(@Param("year") Long year, @Param("status") OrderStatus status);

    @Query("SELECT new com.shose.shoseshop.controller.response.ProductStatisticResponse(" +
            "od.productDetail.product.id, " +
            "od.productDetail.product.name, " +
            "SUM(od.quantity)) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE o.status = :status " +
            "AND YEAR(o.createdAt) = :year " +
            "AND MONTH(o.createdAt) = :month " +
            "GROUP BY od.productDetail.product.id " +
            "ORDER BY SUM(od.quantity) DESC")
    List<ProductStatisticResponse> findProductSalesStatistic(@Param("year") Long year,
                                                             @Param("month") Long month,
                                                             @Param("status") OrderStatus status);

    List<Order> findByUser_Id(Long id);
}

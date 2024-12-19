package com.shose.shoseshop.service;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.constant.PaymentStatus;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.OrderRequest;
import com.shose.shoseshop.controller.response.OrderResponse;
import com.shose.shoseshop.controller.response.ProductStatisticResponse;
import com.shose.shoseshop.controller.response.StatisticResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Long create(OrderRequest orderRequest);
    void update(Long id, OrderStatus status, PaymentStatus paymentStatus);
    Page<OrderResponse> getAll(Pageable pageable, OrderFilterRequest request);
    List<StatisticResponse> statistic(Long year);
    List<ProductStatisticResponse>findProductSalesStatistic(Long month, Long year);
    OrderResponse getById(Long id);
    List<OrderResponse> getByUser();
    void cancelOrder(Long orderId, String reason);
    byte[] exportOrder(Long orderId);
    void updatePaymentStatus(Long orderId);
}

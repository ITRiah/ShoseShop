package com.shose.shoseshop.service;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.OrderRequest;
import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.controller.response.OrderResponse;
import com.shose.shoseshop.controller.response.ProductStatisticResponse;
import com.shose.shoseshop.controller.response.StatisticResponse;
import com.shose.shoseshop.controller.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    void create(OrderRequest orderRequest);
    void update(Long id, OrderStatus status);
    Page<OrderResponse> getAll(Pageable pageable, OrderFilterRequest request);
    List<StatisticResponse> statistic(Long year);
    List<ProductStatisticResponse>findProductSalesStatistic(Long month, Long year);
}

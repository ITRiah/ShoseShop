package com.shose.shoseshop.service;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.request.OrderRequest;

public interface OrderService {
    void create(OrderRequest orderRequest);
    void update(Long id, OrderStatus status);
}

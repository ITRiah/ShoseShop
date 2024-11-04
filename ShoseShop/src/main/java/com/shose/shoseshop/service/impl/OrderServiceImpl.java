package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.request.OrderRequest;
import com.shose.shoseshop.entity.Order;
import com.shose.shoseshop.repository.OrderRepository;
import com.shose.shoseshop.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;

    @Override
    @Transactional
    public void create(OrderRequest orderRequest) {
        orderRepository.save(new ModelMapper().map(orderRequest, Order.class));
    }

    @Override
    @Transactional
    public void update(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        order.setStatus(status);
        orderRepository.save(order);
    }
}

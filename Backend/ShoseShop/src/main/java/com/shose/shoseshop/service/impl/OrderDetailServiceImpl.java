package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.OrderDetailRequest;
import com.shose.shoseshop.entity.OrderDetail;
import com.shose.shoseshop.repository.OrderDetailRepository;
import com.shose.shoseshop.service.OrderDetailService;
import com.shose.shoseshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class OrderDetailServiceImpl implements OrderDetailService {

    OrderDetailRepository orderDetailRepository;
    OrderService orderService;

    @Override
    public void create(OrderDetailRequest orderDetailRequest) {
        orderDetailRepository.save(new ModelMapper().map(orderDetailRequest, OrderDetail.class));
    }
}

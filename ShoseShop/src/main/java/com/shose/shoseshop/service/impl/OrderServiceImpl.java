package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.request.OrderRequest;
import com.shose.shoseshop.controller.response.ProductDetailResponse;
import com.shose.shoseshop.entity.*;
import com.shose.shoseshop.repository.CartDetailRepository;
import com.shose.shoseshop.repository.OrderDetailRepository;
import com.shose.shoseshop.repository.OrderRepository;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    CartDetailRepository cartDetailRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public void create(OrderRequest orderRequest) {
        User user = getUserFromContext();
        Order order = createOrderFromRequest(orderRequest, user);
        List<CartDetail> cartDetails = getCartDetails(orderRequest.getCartDetailIds());
        List<OrderDetail> orderDetails = mapCartDetailsToOrderDetails(cartDetails, order);
        BigDecimal totalAmount = calculateTotalAmount(orderDetails);
        order.setTotalAmount(totalAmount);
        saveOrderAndDetails(order, orderDetails);
    }

    private User getUserFromContext() {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(EntityNotFoundException::new);
    }

    private Order createOrderFromRequest(OrderRequest orderRequest, User user) {
        Order order = new ModelMapper().map(orderRequest, Order.class);
        order.setUser(user);
        return orderRepository.save(order);
    }

    private List<CartDetail> getCartDetails(Set<Long> cartDetailIds) {
        return cartDetailRepository.findByIdIn(cartDetailIds);
    }

    private List<OrderDetail> mapCartDetailsToOrderDetails(List<CartDetail> cartDetails, Order order) {
        return cartDetails.stream()
                .map(cartDetail -> {
                    OrderDetail orderDetail = new ModelMapper().map(cartDetail, OrderDetail.class);
                    orderDetail.setOrder(order);
                    return orderDetail;
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalAmount(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(orderDetail -> orderDetail.getProductDetail().getPrice()
                        .multiply(BigDecimal.valueOf(orderDetail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void saveOrderAndDetails(Order order, List<OrderDetail> orderDetails) {
        orderDetailRepository.saveAll(orderDetails);
        orderRepository.save(order);
    }


    @Override
    @Transactional
    public void update(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        order.setStatus(status);
        orderRepository.save(order);
    }
}

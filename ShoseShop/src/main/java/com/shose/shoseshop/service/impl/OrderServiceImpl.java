package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.OrderRequest;
import com.shose.shoseshop.controller.response.OrderResponse;
import com.shose.shoseshop.controller.response.ProductStatisticResponse;
import com.shose.shoseshop.controller.response.StatisticResponse;
import com.shose.shoseshop.controller.response.UserResponse;
import com.shose.shoseshop.entity.*;
import com.shose.shoseshop.repository.*;
import com.shose.shoseshop.service.OrderService;
import com.shose.shoseshop.specification.OrderSpecification;
import com.shose.shoseshop.specification.UserSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
    VoucherRepository voucherRepository;
    ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(OrderRequest orderRequest) {
        User user = getUserFromContext();
        Order order = createOrderFromRequest(orderRequest, user);
        List<CartDetail> cartDetails = getCartDetails(orderRequest.getCartDetailIds());
        List<OrderDetail> orderDetails = mapCartDetailsToOrderDetails(cartDetails, order);
        BigDecimal totalAmount = calculateTotalAmount(orderDetails, orderRequest);
        order.setTotalAmount(totalAmount);
        saveOrderAndDetailsAndCartDetails(order, orderDetails, cartDetails);
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

    private BigDecimal calculateTotalAmount(List<OrderDetail> orderDetails, OrderRequest orderRequest) {
        BigDecimal total = orderDetails.stream()
                .map(orderDetail -> orderDetail.getProductDetail().getPrice()
                        .multiply(BigDecimal.valueOf(orderDetail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (orderRequest.getVoucherId() != null) {
            Voucher voucher = voucherRepository.findById(orderRequest.getVoucherId()).orElseThrow(EntityNotFoundException::new);
            int value = voucher.getValue();
            BigDecimal maxMoney = voucher.getMaxMoney();
            if (total.multiply(BigDecimal.valueOf(value / 100)).compareTo(maxMoney) > 0) {
                total = total.subtract(maxMoney);
            } else {
                total = total.subtract(total.multiply(BigDecimal.valueOf(value / 100)));
            }
        }
        return total;
    }

    private void saveOrderAndDetailsAndCartDetails(Order order, List<OrderDetail> orderDetails, List<CartDetail> cartDetails) {
        cartDetails.forEach(BaseEntity::markAsDelete);
        cartDetailRepository.saveAll(cartDetails);
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

    @Override
    public Page<OrderResponse> getAll(Pageable pageable, OrderFilterRequest request) {
        Specification<Order> spec = OrderSpecification.generateFilter(request);
        Page<Order> orderPage = orderRepository.findAll(spec, pageable);
        return orderPage.map(order -> modelMapper.map(order, OrderResponse.class));
    }

    @Override
    public List<StatisticResponse> statistic(Long year) {
        return orderRepository.findMonthlyRevenue(year, OrderStatus.DELIVERED);
    }

    @Override
    public List<ProductStatisticResponse> findProductSalesStatistic(Long month, Long year) {
        return orderRepository.findProductSalesStatistic(month, year, OrderStatus.DELIVERED);
    }

    @Override
    public OrderResponse getById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getByUser() {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(loginUser.getUsername()).orElseThrow(EntityNotFoundException::new);
        List<Order> orders = orderRepository.findByUser_Id(user.getId());
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .collect(Collectors.toList());    }
}

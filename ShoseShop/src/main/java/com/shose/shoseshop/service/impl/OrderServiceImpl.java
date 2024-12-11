package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.OrderRequest;
import com.shose.shoseshop.controller.response.OrderResponse;
import com.shose.shoseshop.controller.response.ProductStatisticResponse;
import com.shose.shoseshop.controller.response.StatisticResponse;
import com.shose.shoseshop.entity.*;
import com.shose.shoseshop.repository.*;
import com.shose.shoseshop.service.CartService;
import com.shose.shoseshop.service.EmailService;
import com.shose.shoseshop.service.OrderService;
import com.shose.shoseshop.specification.OrderSpecification;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    CartDetailRepository cartDetailRepository;
    CartService cartService;
    UserRepository userRepository;
    VoucherRepository voucherRepository;
    ModelMapper modelMapper;
    EmailService emailService;
    ProductDetailRepository productDetailRepository;

    @Override
    @Transactional
    public void create(OrderRequest orderRequest) {
        User user = getUserFromContext();
        Order order = createOrderFromRequest(orderRequest, user);
        List<CartDetail> cartDetails = getCartDetails(orderRequest.getCartDetailIds());
        List<OrderDetail> orderDetails = mapCartDetailsToOrderDetails(cartDetails, order);
        BigDecimal totalAmount = calculateTotalAmount(orderDetails, orderRequest);
        order.setTotalAmount(totalAmount);
        saveOrderAndDetailsAndCartDetails(order, orderDetails, cartDetails, orderRequest.getCartDetailIds());
        try {
            emailService.sendInvoiceWithAttachment(user.getEmail(), order.getFullName(), totalAmount, orderDetails);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private User getUserFromContext() {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(EntityNotFoundException::new);
    }

    private Order createOrderFromRequest(OrderRequest orderRequest, User user) {
        Order order = new ModelMapper().map(orderRequest, Order.class);
        order.setUser(user);
        order.setId(null);
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
            if (voucher.getQuantity() <= 0) {
                throw new IllegalArgumentException("This voucher is out of stock!");
            }
            int value = voucher.getValue();
            BigDecimal maxMoney = voucher.getMaxMoney();
            if (total.multiply(BigDecimal.valueOf(value).divide(BigDecimal.valueOf(100))).compareTo(maxMoney) > 0) {
                total = total.subtract(maxMoney);
            } else {
                total = total.subtract(total.multiply(BigDecimal.valueOf(value).divide(BigDecimal.valueOf(100))));
            }
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherRepository.save(voucher);
        }
        return total;
    }

    private void saveOrderAndDetailsAndCartDetails(Order order, List<OrderDetail> orderDetails, List<CartDetail> cartDetails, Set<Long> cartDetailIds) {
        List<ProductDetail> productDetails = new ArrayList<>();
        for (CartDetail cartDetail : cartDetails) {
            ProductDetail productDetail = cartDetail.getProductDetail();
            int remainingQuantity = productDetail.getQuantity() - cartDetail.getQuantity().intValue();
            if (remainingQuantity < 0) {
                throw new IllegalArgumentException("Insufficient stock for product: " + productDetail.getId());
            }
            productDetail.setQuantity(remainingQuantity);
            productDetails.add(productDetail);
        }
        cartDetails.forEach(BaseEntity::markAsDelete);
        productDetailRepository.saveAll(productDetails);
        cartDetailRepository.saveAll(cartDetails);
        orderDetailRepository.saveAll(orderDetails);
        orderRepository.save(order);
        cartService.deleteCartDetails(cartDetailIds);
    }

    @Override
    @Transactional
    public void update(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        OrderStatus currentStatus = order.getStatus();
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new IllegalArgumentException("Invalid status transition: " + currentStatus + " -> " + newStatus);
        }
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        switch (currentStatus) {
            case PENDING:
                return newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELED;
            case CONFIRMED:
                return newStatus == OrderStatus.PROCESSING || newStatus == OrderStatus.CANCELED;
            case PROCESSING:
                return newStatus == OrderStatus.SHIPPED;
            case SHIPPED:
                return newStatus == OrderStatus.DELIVERED;
            case DELIVERED:
            case CANCELED:
                return false;
            default:
                return false;
        }
    }


    @Override
    public Page<OrderResponse> getAll(Pageable pageable, OrderFilterRequest request) {
        Specification<Order> spec = OrderSpecification.generateFilter(request);
        Page<Order> orderPage = orderRepository.findAll(spec, pageable);
        return orderPage.map(order -> modelMapper.map(order, OrderResponse.class));
    }

    @Override
    public List<StatisticResponse> statistic(Long year) {
        List<StatisticResponse> rawData = orderRepository.findMonthlyRevenue(year, OrderStatus.DELIVERED);
        Map<Integer, StatisticResponse> resultMap = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            resultMap.put(month, new StatisticResponse(year.intValue(), month, BigDecimal.ZERO));
        }
        for (StatisticResponse data : rawData) {
            resultMap.put(data.getMonth(), data);
        }
        return resultMap.values().stream()
                .sorted(Comparator.comparing(StatisticResponse::getMonth))
                .toList();
    }


    @Override
    public List<ProductStatisticResponse> findProductSalesStatistic(Long month, Long year) {
        List<ProductStatisticResponse> fullList = orderRepository.findProductSalesStatistic(year, month, OrderStatus.DELIVERED);
        return fullList.size() > 5 ? fullList.subList(0, 5) : fullList;
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
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        if (!order.getStatus().equals(OrderStatus.PENDING) && !order.getStatus().equals(OrderStatus.CONFIRMED)) {
            throw new IllegalArgumentException("Order status must be PENDING!");
        }
        if (order.getVoucherId() != null) {
            Voucher voucher = voucherRepository.findById(order.getVoucherId()).orElseThrow(EntityNotFoundException::new);
            voucher.setQuantity(voucher.getQuantity() + 1);
            voucherRepository.save(voucher);
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder_Id(orderId);
        List<ProductDetail> updatedProductDetails = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            ProductDetail productDetail = orderDetail.getProductDetail();
            productDetail.setQuantity(productDetail.getQuantity() + orderDetail.getQuantity().intValue());
            updatedProductDetails.add(productDetail);
        }
        productDetailRepository.saveAll(updatedProductDetails);
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }
}

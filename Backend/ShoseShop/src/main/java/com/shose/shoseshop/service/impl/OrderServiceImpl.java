package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.constant.PaymentStatus;
import com.shose.shoseshop.constant.ShippingMethod;
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
    public Long create(OrderRequest orderRequest) {
        User user = getUserFromContext();
        Order order = createOrderFromRequest(orderRequest, user);
        List<CartDetail> cartDetails = getCartDetails(orderRequest.getCartDetailIds());
        List<OrderDetail> orderDetails = mapCartDetailsToOrderDetails(cartDetails, order);
        BigDecimal totalAmount = calculateTotalAmount(orderDetails, orderRequest);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.WAITING);
        Long id = saveOrderAndDetailsAndCartDetails(order, orderDetails, cartDetails, orderRequest.getCartDetailIds());
        try {
            order.setOrderDetails(orderDetails);
            emailService.sendInvoiceWithAttachment(user.getEmail(), order);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return id;
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
        if (orderRequest.getShippingMethod() == ShippingMethod.FAST) {
            total = total.add(new BigDecimal("15000"));
        } else {
            total = total.add(new BigDecimal("50000"));
        }
        if (orderRequest.getVoucherId() != null) {
            Voucher voucher = voucherRepository.findById(orderRequest.getVoucherId()).orElseThrow(EntityNotFoundException::new);
            if (voucher.getQuantity() <= 0) {
                throw new IllegalArgumentException("Voucher đã hết lượt sử dụng!");
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

    private Long saveOrderAndDetailsAndCartDetails(Order order, List<OrderDetail> orderDetails, List<CartDetail> cartDetails, Set<Long> cartDetailIds) {
        List<ProductDetail> productDetails = new ArrayList<>();
        for (CartDetail cartDetail : cartDetails) {
            ProductDetail productDetail = cartDetail.getProductDetail();
            int remainingQuantity = productDetail.getQuantity() - cartDetail.getQuantity().intValue();
            if (remainingQuantity < 0) {
                throw new IllegalArgumentException("Sản phẩm này đã hết hàng!");
            }
            productDetail.setQuantity(remainingQuantity);
            productDetails.add(productDetail);
        }
        cartDetails.forEach(BaseEntity::markAsDelete);
        productDetailRepository.saveAll(productDetails);
        cartDetailRepository.saveAll(cartDetails);
        orderDetailRepository.saveAll(orderDetails);
        Order orderSave = orderRepository.save(order);
        cartService.deleteCartDetails(cartDetailIds);
        return orderSave.getId();
    }

    @Override
    @Transactional
    public void update(Long id, OrderStatus newStatus, PaymentStatus paymentStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        OrderStatus currentStatus = order.getStatus();
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new IllegalArgumentException("Không thể cập nhật trạng thái đơn hàng từ : " + currentStatus.getValue() + " -> " + newStatus.getValue());
        }
        order.setStatus(newStatus);
        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
    }

    private boolean isValidStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        if (newStatus == currentStatus) return true;
        return switch (currentStatus) {
            case PENDING -> newStatus == OrderStatus.CONFIRMED || newStatus == OrderStatus.CANCELED;
            case CONFIRMED -> newStatus == OrderStatus.PROCESSING;
            case PROCESSING -> newStatus == OrderStatus.SHIPPED;
            case SHIPPED -> newStatus == OrderStatus.DELIVERED;
            case DELIVERED, CANCELED -> false;
            default -> false;
        };
    }


    @Override
    public Page<OrderResponse> getAll(Pageable pageable, OrderFilterRequest request) {
        Specification<Order> spec = OrderSpecification.generateFilter(request);
        Page<Order> orderPage = orderRepository.findAll(spec, pageable);
        Map<Long, String> productNameByProductDetailId = productDetailRepository.findAll()
                .stream().collect(Collectors.toMap(ProductDetail::getId, productDetail -> productDetail.getProduct().getName()));

        return orderPage.map(order -> {
            OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);
            orderResponse.getOrderDetails().forEach(orderDetail -> {
              orderDetail.setProductName(productNameByProductDetailId.get(orderDetail.getProductDetail().getId()));
            });
            return orderResponse;
        });
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
    public void cancelOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new IllegalArgumentException("Trạng thái đơn hàng phải không hợp lệ!");
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
        order.setReason(reason);
        orderRepository.save(order);
    }

    @Override
    public byte[] exportOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        return emailService.generateInvoicePdf(order);
    }

    @Override
    @Transactional
    public void updatePaymentStatus(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.setPaymentStatus(PaymentStatus.COMPLETE);
        orderRepository.save(order);
    }
}

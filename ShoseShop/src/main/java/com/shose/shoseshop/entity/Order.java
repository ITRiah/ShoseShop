package com.shose.shoseshop.entity;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.constant.PaymentMethod;
import com.shose.shoseshop.constant.ShippingMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Where(clause = "is_deleted = false")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fullName;
    private String phone;
    private OrderStatus status = OrderStatus.PENDING;
    private String reason;
    private LocalDateTime orderDate = LocalDateTime.now();
    private String shippingAddress;
    private ShippingMethod shippingMethod;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private String note;
}

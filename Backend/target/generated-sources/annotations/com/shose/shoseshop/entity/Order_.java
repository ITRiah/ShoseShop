package com.shose.shoseshop.entity;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.constant.PaymentMethod;
import com.shose.shoseshop.constant.ShippingMethod;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Order.class)
public abstract class Order_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile SingularAttribute<Order, String> reason;
	public static volatile SingularAttribute<Order, BigDecimal> totalAmount;
	public static volatile SingularAttribute<Order, String> note;
	public static volatile ListAttribute<Order, OrderDetail> orderDetails;
	public static volatile SingularAttribute<Order, String> phone;
	public static volatile SingularAttribute<Order, ShippingMethod> shippingMethod;
	public static volatile SingularAttribute<Order, String> fullName;
	public static volatile SingularAttribute<Order, String> shippingAddress;
	public static volatile SingularAttribute<Order, PaymentMethod> paymentMethod;
	public static volatile SingularAttribute<Order, Long> id;
	public static volatile SingularAttribute<Order, User> user;
	public static volatile SingularAttribute<Order, OrderStatus> status;

	public static final String REASON = "reason";
	public static final String TOTAL_AMOUNT = "totalAmount";
	public static final String NOTE = "note";
	public static final String ORDER_DETAILS = "orderDetails";
	public static final String PHONE = "phone";
	public static final String SHIPPING_METHOD = "shippingMethod";
	public static final String FULL_NAME = "fullName";
	public static final String SHIPPING_ADDRESS = "shippingAddress";
	public static final String PAYMENT_METHOD = "paymentMethod";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String STATUS = "status";

}


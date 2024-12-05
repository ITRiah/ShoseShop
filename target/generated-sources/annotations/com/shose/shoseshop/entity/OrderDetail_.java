package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrderDetail.class)
public abstract class OrderDetail_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile SingularAttribute<OrderDetail, Long> quantity;
	public static volatile SingularAttribute<OrderDetail, ProductDetail> productDetail;
	public static volatile SingularAttribute<OrderDetail, Long> id;
	public static volatile SingularAttribute<OrderDetail, Order> order;

	public static final String QUANTITY = "quantity";
	public static final String PRODUCT_DETAIL = "productDetail";
	public static final String ID = "id";
	public static final String ORDER = "order";

}


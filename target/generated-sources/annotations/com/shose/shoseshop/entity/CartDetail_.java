package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CartDetail.class)
public abstract class CartDetail_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile SingularAttribute<CartDetail, Long> quantity;
	public static volatile SingularAttribute<CartDetail, ProductDetail> productDetail;
	public static volatile SingularAttribute<CartDetail, Long> cartId;
	public static volatile SingularAttribute<CartDetail, Long> id;

	public static final String QUANTITY = "quantity";
	public static final String PRODUCT_DETAIL = "productDetail";
	public static final String CART_ID = "cartId";
	public static final String ID = "id";

}


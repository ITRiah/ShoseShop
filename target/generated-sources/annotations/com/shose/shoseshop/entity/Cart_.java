package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Cart.class)
public abstract class Cart_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile ListAttribute<Cart, CartDetail> cartDetails;
	public static volatile SingularAttribute<Cart, Long> id;
	public static volatile SingularAttribute<Cart, User> user;

	public static final String CART_DETAILS = "cartDetails";
	public static final String ID = "id";
	public static final String USER = "user";

}


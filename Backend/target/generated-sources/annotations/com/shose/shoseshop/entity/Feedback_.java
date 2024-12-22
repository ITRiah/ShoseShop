package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Feedback.class)
public abstract class Feedback_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile SingularAttribute<Feedback, Product> product;
	public static volatile SingularAttribute<Feedback, String> img;
	public static volatile SingularAttribute<Feedback, Integer> rate;
	public static volatile SingularAttribute<Feedback, String> description;
	public static volatile SingularAttribute<Feedback, Long> id;
	public static volatile SingularAttribute<Feedback, User> user;
	public static volatile SingularAttribute<Feedback, Order> order;

	public static final String PRODUCT = "product";
	public static final String IMG = "img";
	public static final String RATE = "rate";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String USER = "user";
	public static final String ORDER = "order";

}


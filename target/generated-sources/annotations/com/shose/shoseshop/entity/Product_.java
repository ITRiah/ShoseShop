package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile SingularAttribute<Product, String> img;
	public static volatile SingularAttribute<Product, Float> star;
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, String> description;
	public static volatile SingularAttribute<Product, Long> id;
	public static volatile SingularAttribute<Product, Procedure> procedure;
	public static volatile SingularAttribute<Product, Category> category;
	public static volatile ListAttribute<Product, ProductDetail> productDetailResponseList;
	public static volatile SingularAttribute<Product, BigDecimal> priceRange;

	public static final String IMG = "img";
	public static final String STAR = "star";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String PROCEDURE = "procedure";
	public static final String CATEGORY = "category";
	public static final String PRODUCT_DETAIL_RESPONSE_LIST = "productDetailResponseList";
	public static final String PRICE_RANGE = "priceRange";

}


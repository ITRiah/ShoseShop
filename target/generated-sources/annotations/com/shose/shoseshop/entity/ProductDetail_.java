package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductDetail.class)
public abstract class ProductDetail_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile SingularAttribute<ProductDetail, Product> product;
	public static volatile SingularAttribute<ProductDetail, String> img;
	public static volatile SingularAttribute<ProductDetail, Integer> quantity;
	public static volatile SingularAttribute<ProductDetail, String> color;
	public static volatile SingularAttribute<ProductDetail, String> size;
	public static volatile SingularAttribute<ProductDetail, BigDecimal> price;
	public static volatile SingularAttribute<ProductDetail, Long> id;

	public static final String PRODUCT = "product";
	public static final String IMG = "img";
	public static final String QUANTITY = "quantity";
	public static final String COLOR = "color";
	public static final String SIZE = "size";
	public static final String PRICE = "price";
	public static final String ID = "id";

}


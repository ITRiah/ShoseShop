package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Category.class)
public abstract class Category_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile SingularAttribute<Category, String> img;
	public static volatile SingularAttribute<Category, String> name;
	public static volatile SingularAttribute<Category, Long> id;

	public static final String IMG = "img";
	public static final String NAME = "name";
	public static final String ID = "id";

}


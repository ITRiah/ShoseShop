package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Procedure.class)
public abstract class Procedure_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile SingularAttribute<Procedure, String> img;
	public static volatile SingularAttribute<Procedure, String> name;
	public static volatile SingularAttribute<Procedure, Long> id;

	public static final String IMG = "img";
	public static final String NAME = "name";
	public static final String ID = "id";

}


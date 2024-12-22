package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OTP.class)
public abstract class OTP_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile SingularAttribute<OTP, String> otp;
	public static volatile SingularAttribute<OTP, Long> id;
	public static volatile SingularAttribute<OTP, String> email;

	public static final String OTP = "otp";
	public static final String ID = "id";
	public static final String EMAIL = "email";

}


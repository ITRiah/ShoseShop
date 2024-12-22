package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Voucher.class)
public abstract class Voucher_ extends com.shose.shoseshop.entity.BaseEntity_ {

	public static volatile SingularAttribute<Voucher, String> img;
	public static volatile SingularAttribute<Voucher, String> code;
	public static volatile SingularAttribute<Voucher, Integer> quantity;
	public static volatile SingularAttribute<Voucher, String> description;
	public static volatile SingularAttribute<Voucher, Long> id;
	public static volatile SingularAttribute<Voucher, Integer> value;
	public static volatile SingularAttribute<Voucher, Date> expiredTime;
	public static volatile SingularAttribute<Voucher, BigDecimal> maxMoney;

	public static final String IMG = "img";
	public static final String CODE = "code";
	public static final String QUANTITY = "quantity";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String VALUE = "value";
	public static final String EXPIRED_TIME = "expiredTime";
	public static final String MAX_MONEY = "maxMoney";

}


package com.shose.shoseshop.entity;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.Instant;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BaseEntity.class)
public abstract class BaseEntity_ {

	public static volatile SingularAttribute<BaseEntity, Instant> createdAt;
	public static volatile SingularAttribute<BaseEntity, Instant> lastModifiedAt;
	public static volatile SingularAttribute<BaseEntity, Boolean> isDeleted;
	public static volatile SingularAttribute<BaseEntity, String> createdBy;
	public static volatile SingularAttribute<BaseEntity, String> lastModifiedBy;

	public static final String CREATED_AT = "createdAt";
	public static final String LAST_MODIFIED_AT = "lastModifiedAt";
	public static final String IS_DELETED = "isDeleted";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";

}


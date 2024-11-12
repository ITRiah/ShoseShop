package com.shose.shoseshop.util;

import jakarta.persistence.criteria.*;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
@UtilityClass
public class QueryUtils {

    public interface IExpressionBuilder<Domain, SubQueryDomain> {
        Expression<Boolean> build(Root<Domain> root, Subquery<SubQueryDomain> subQuery, Root<SubQueryDomain> subRoot);
    }

    public static Predicate and(CriteriaBuilder cb, Predicate... var1) {
        List<Predicate> all = CommonUtilQuery.findAll(Arrays.asList(var1), (index, item) -> item != null);
        Predicate predicate = null;
        if (Boolean.TRUE.equals(CommonUtilQuery.isTrue(all))) {
            predicate = cb.and(all.toArray(new Predicate[]{}));
        }
        return predicate;
    }

    public static Predicate or(CriteriaBuilder cb, Predicate... var1) {
        List<Predicate> all = CommonUtilQuery.findAll(Arrays.asList(var1), (index, item) -> item != null);
        Predicate predicate = null;
        if (Boolean.TRUE.equals(CommonUtilQuery.isTrue(all))) {
            predicate = cb.or(all.toArray(new Predicate[]{}));
        }
        return predicate;
    }

    public static <T, P> Predicate buildEqFilter(Root<T> root, CriteriaBuilder cb, String fieldName, P value) {
        Predicate predicate = null;
        if (Boolean.TRUE.equals(CommonUtilQuery.isTrue(value))) {
            predicate = cb.equal(root.get(fieldName), value);
        }
        return predicate;
    }

    public static <Domain, SubQueryDomain> Subquery<SubQueryDomain> buildSubQuery(
            Class<SubQueryDomain> subQueryDomainClass, Root<Domain> root, CriteriaQuery<?> criteriaQuery,
            IExpressionBuilder<Domain, SubQueryDomain> expressionBuilder) {
        Subquery<SubQueryDomain> subQuery = criteriaQuery.subquery(subQueryDomainClass);
        Root<SubQueryDomain> subRoot = subQuery.from(subQueryDomainClass);
        return subQuery.select(subRoot).where(expressionBuilder.build(root, subQuery, subRoot));
    }
}

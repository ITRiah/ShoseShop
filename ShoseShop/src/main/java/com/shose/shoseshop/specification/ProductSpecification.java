package com.shose.shoseshop.specification;

import com.shose.shoseshop.constant.Role;
import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.entity.*;
import com.shose.shoseshop.util.QueryUtils;
import com.shose.shoseshop.util.StringUtil;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Set;

public class ProductSpecification {
    private static Specification<Product> hasProcedureIdIn(Set<Long> procedureIds) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.exists(
                QueryUtils.buildSubQuery(
                        Procedure.class, root, query, (Root<Product> rootParent, Subquery<Procedure> subQuery, Root<Procedure> subRoot)
                                -> QueryUtils.and(cb,
                                cb.equal(subRoot.get(Procedure_.ID), root.get(Product_.PROCEDURE).get(Procedure_.ID)),
                                subRoot.get(Procedure_.ID).in(procedureIds))
                ));
    }

    private static Specification<Product> hasCategoryIdIn(Set<Long> categoryIds) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.exists(
                QueryUtils.buildSubQuery(
                        Category.class, root, query, (Root<Product> rootParent, Subquery<Category> subQuery, Root<Category> subRoot)
                                -> QueryUtils.and(cb,
                                cb.equal(subRoot.get(Category_.ID), root.get(Product_.CATEGORY).get(Category_.ID)),
                                subRoot.get(Category_.ID).in(categoryIds))
                ));
    }


    private static Specification<Product> isDeleted() {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.isFalse(root.get(Product_.IS_DELETED));
    }

    private static Specification<Product> hasName(String name) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Path<String> namePath = root.get(Product_.NAME);
            return cb.like(namePath, StringUtil.toLike(name));
        };
    }

    private static Specification<Product> hasPriceBigger(BigDecimal priceBigger) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.greaterThanOrEqualTo(root.get(Product_.PRICE_RANGE), priceBigger);
    }

    private static Specification<Product> hasPriceLower(BigDecimal priceLower) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.lessThanOrEqualTo(root.get(Product_.PRICE_RANGE), priceLower);
    }

    public static Specification<Product> generateFilterProducts(ProductFilterRequest request) {
        Specification<Product> specification = Specification.where(null);
        if (request == null) return specification;
        if (request.getProcedureIds() != null && !request.getProcedureIds().isEmpty()) {
            specification = specification.and(hasProcedureIdIn(request.getProcedureIds()));
        }
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            specification = specification.and(hasCategoryIdIn(request.getCategoryIds()));
        }
        if (request.getRole() != null && request.getRole().equals(Role.USER)) {
            specification = specification.and(isDeleted());
        }
        if (request.getName() != null) {
            specification = specification.and(hasName(request.getName()));
        }
        if (request.getPriceBigger() != null) {
            specification = specification.and(hasPriceBigger(request.getPriceBigger()));
        }
        if (request.getPriceLower() != null) {
            specification = specification.and(hasPriceLower(request.getPriceLower()));
        }
        return specification;
    }
}

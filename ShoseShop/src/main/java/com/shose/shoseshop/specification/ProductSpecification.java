package com.shose.shoseshop.specification;

import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.entity.Product_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public final class ProductSpecification {

    public static Specification<Product> hasProcedureIdIn(Set<Long> procedureIds) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (procedureIds == null || procedureIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join(Product_.PROCEDURE)
                    .get("id")
                    .in(procedureIds);
        };
    }

    public static Specification<Product> generateFilterProducts(ProductFilterRequest request) {
        Specification<Product> specification = Specification.where(null);

        if (request.getProcedureIds() != null && !request.getProcedureIds().isEmpty()) {
            specification = specification.and(hasProcedureIdIn(request.getProcedureIds()));
        }

        return specification;
    }
}

package com.shose.shoseshop.specification;

import com.shose.shoseshop.constant.Role;
import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.entity.*;
import com.shose.shoseshop.entity.Order;
import com.shose.shoseshop.util.QueryUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.Set;

public class ProductSpecification {
    private static Specification<Product> hasProcedureIdIn(Set<Long> procedureIds) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.exists(
                QueryUtils.buildSubQuery(
                        Procedure.class, root, query, (Root<Product> rootParent, Subquery<Procedure> subQuery, Root<Procedure> subRoot)
                                -> QueryUtils.and(cb, cb.equal(subRoot.get(Procedure_.ID), root.get(Product_.PROCEDURE)),
                                subRoot.get(Procedure_.ID).in(procedureIds))));
    }

    private static Specification<Product> isDeleted() {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.isFalse(root.get(Product_.IS_DELETED));
    }

    public static Specification<Product> generateFilterProducts(ProductFilterRequest request) {
        Specification<Product> specification = Specification.where(null);
        if (request == null) return specification;
        if (request.getProcedureIds() != null && !request.getProcedureIds().isEmpty()) {
            specification = specification.and(hasProcedureIdIn(request.getProcedureIds()));
        }
        if (request.getRole() != null && request.getRole().equals(Role.USER)) {
            specification = specification.and(isDeleted());
        }
        return specification;
    }
}

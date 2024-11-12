package com.shose.shoseshop.specification;

import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.entity.Procedure;
import com.shose.shoseshop.entity.Procedure_;
import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.entity.Product_;
import com.shose.shoseshop.util.QueryUtils;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class ProductSpecification {
    private static Specification<Product> hasProcedureIdIn(Set<Long> procedureIds) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.exists(
                QueryUtils.buildSubQuery(
                        Procedure.class, root, query, (Root<Product> rootParent, Subquery<Procedure> subQuery, Root<Procedure> subRoot)
                                -> QueryUtils.and(cb, cb.equal(subRoot.get(Procedure_.ID), root.get(Product_.PROCEDURE)),
                                subRoot.get(Procedure_.ID).in(procedureIds))));
    }

    public static Specification<Product> generateFilterProducts(ProductFilterRequest request) {
        Specification<Product> specification = Specification.where(null);

        if (request.getProcedureIds() != null && !request.getProcedureIds().isEmpty()) {
            specification = specification.and(hasProcedureIdIn(request.getProcedureIds()));
        }

        return specification;
    }
}

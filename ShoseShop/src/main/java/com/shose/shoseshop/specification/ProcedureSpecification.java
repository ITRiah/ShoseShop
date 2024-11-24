package com.shose.shoseshop.specification;

import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.entity.Procedure;
import com.shose.shoseshop.entity.Procedure_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ProcedureSpecification {
    private static Specification<Procedure> hasFullname(String name) {
        return (Root<Procedure> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Path<String> fullNamePath = root.get(Procedure_.NAME);
            return cb.like(fullNamePath, name);
        };
    }

    private static Specification<Procedure> hasDateFrom(Date dateFrom) {
        return (Root<Procedure> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.greaterThanOrEqualTo(root.get(Procedure_.CREATED_AT), dateFrom);
    }

    private static Specification<Procedure> hasDateTo(Date dateTo) {
        return (Root<Procedure> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.lessThanOrEqualTo(root.get(Procedure_.CREATED_AT), dateTo);
    }

    public static Specification<Procedure> generateFilter(OrderFilterRequest request) {
        Specification<Procedure> specification = Specification.where(null);
        if (request == null) return specification;
        if (request.getName() != null) {
            specification = specification.and(hasFullname(request.getName()));
        }
        if (request.getDateFrom() != null) {
            specification = specification.and((hasDateFrom(request.getDateFrom())));
        }
        if (request.getDateTo() != null) {
            specification = specification.and((hasDateTo(request.getDateTo())));
        }
        return specification;
    }
}

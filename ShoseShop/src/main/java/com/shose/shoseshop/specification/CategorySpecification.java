package com.shose.shoseshop.specification;

import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.entity.Category;
import com.shose.shoseshop.entity.Category_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class CategorySpecification {
    private static Specification<Category> hasFullname(String name) {
        return (Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Path<String> fullNamePath = root.get(Category_.NAME);
            return cb.like(fullNamePath, name);
        };
    }

    private static Specification<Category> hasDateFrom(Date dateFrom) {
        return (Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.greaterThanOrEqualTo(root.get(Category_.CREATED_AT), dateFrom);
    }

    private static Specification<Category> hasDateTo(Date dateTo) {
        return (Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.lessThanOrEqualTo(root.get(Category_.CREATED_AT), dateTo);
    }

    public static Specification<Category> generateFilter(OrderFilterRequest request) {
        Specification<Category> specification = Specification.where(null);
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

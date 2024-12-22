package com.shose.shoseshop.specification;

import com.shose.shoseshop.constant.Role;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.entity.Category;
import com.shose.shoseshop.entity.Category_;
import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.entity.Product_;
import com.shose.shoseshop.util.StringUtil;
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
            return cb.like(fullNamePath, StringUtil.toLike(name));
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

    private static Specification<Category> isDeleted() {
        return (Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.isFalse(root.get(Category_.IS_DELETED));
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
        if (request.getRole() != null && request.getRole().equals(Role.USER)) {
            specification = specification.and(isDeleted());
        }
        return specification;
    }
}

package com.shose.shoseshop.specification;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.entity.Order;
import com.shose.shoseshop.entity.Order_;
import com.shose.shoseshop.util.StringUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class OrderSpecification {
    private static Specification<Order> hasFullname(String fullName) {
        return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Path<String> fullNamePath = root.get(Order_.FULL_NAME);
            return cb.like(fullNamePath, StringUtil.toLike(fullName));
        };
    }

    private static Specification<Order> hasDateFrom(Date dateFrom) {
        return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.greaterThanOrEqualTo(root.get(Order_.CREATED_AT), dateFrom);
    }

    private static Specification<Order> hasDateTo(Date dateTo) {
        return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.lessThanOrEqualTo(root.get(Order_.CREATED_AT), dateTo);
    }

    private static Specification<Order> hasStatus(OrderStatus status) {
        return (Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Path<String> statusPath = root.get(Order_.STATUS);
            return cb.equal(statusPath, status);
        };
    }

    public static Specification<Order> generateFilter(OrderFilterRequest request) {
        Specification<Order> specification = Specification.where(null);
        if (request == null) return specification;
        if (request.getFullName() != null) {
            specification = specification.and(hasFullname(request.getFullName()));
        }
        if (request.getDateFrom() != null) {
            specification = specification.and((hasDateFrom(request.getDateFrom())));
        }
        if (request.getDateTo() != null) {
            specification = specification.and((hasDateTo(request.getDateTo())));
        }
        if (request.getStatus() != null) {
            specification = specification.and((hasStatus(request.getStatus())));
        }
        return specification;
    }
}

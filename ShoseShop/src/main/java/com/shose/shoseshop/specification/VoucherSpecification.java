package com.shose.shoseshop.specification;

import com.shose.shoseshop.constant.Role;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.entity.Product_;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.entity.Voucher;
import com.shose.shoseshop.entity.Voucher_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class VoucherSpecification {
    private static Specification<Voucher> hasDateFrom(Date dateFrom) {
        return (Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.greaterThanOrEqualTo(root.get(Voucher_.CREATED_AT), dateFrom);
    }

    private static Specification<Voucher> hasDateTo(Date dateTo) {
        return (Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.lessThanOrEqualTo(root.get(Voucher_.CREATED_AT), dateTo);
    }

    private static Specification<Voucher> isDeleted() {
        return (Root<Voucher> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.isFalse(root.get(Product_.IS_DELETED));
    }

    public static Specification<Voucher> generateFilter(OrderFilterRequest request) {
        Specification<Voucher> specification = Specification.where(null);
        if (request == null) return specification;
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

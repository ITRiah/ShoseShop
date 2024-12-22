package com.shose.shoseshop.specification;

import com.shose.shoseshop.constant.Role;
import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.entity.Product_;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.entity.User_;
import com.shose.shoseshop.util.StringUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    private static Specification<User> hasEmail(String email) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Path<String> emailPath = root.get(User_.EMAIL);
            return cb.like(emailPath, StringUtil.toLike(email));
        };
    }

    private static Specification<User> hasUserName(String userName) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Path<String> emailPath = root.get(User_.USERNAME);
            return cb.like(emailPath, StringUtil.toLike(userName));
        };
    }

    private static Specification<User> isDeleted() {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb)
                -> cb.isFalse(root.get(Product_.IS_DELETED));
    }

    public static Specification<User> generateFilter(UserFilterRequest request) {
        Specification<User> specification = Specification.where(null);
        if (request == null) return specification;
        if (request.getEmail() != null) {
            specification = specification.and((hasUserName(request.getEmail()))).or(hasEmail(request.getEmail()));
        }
        if (request.getRole() != null && request.getRole().equals(Role.USER)) {
            specification = specification.and(isDeleted());
        }
        return specification;
    }
}

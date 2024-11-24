package com.shose.shoseshop.specification;

import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.entity.User_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    private static Specification<User> hasEmail(String email) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Path<String> emailPath = root.get(User_.EMAIL);
            return cb.like(emailPath, email);
        };
    }

    private static Specification<User> hasUserName(String userName) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Path<String> emailPath = root.get(User_.USERNAME);
            return cb.like(emailPath, userName);
        };
    }

    public static Specification<User> generateFilter(UserFilterRequest request) {
        Specification<User> specification = Specification.where(null);
        if (request == null) return specification;
        if (request.getUserName() != null) {
            specification = specification.and(hasEmail(request.getEmail()));
        }
        if (request.getEmail() != null) {
            specification = specification.and((hasUserName(request.getUserName())));
        }
        return specification;
    }
}

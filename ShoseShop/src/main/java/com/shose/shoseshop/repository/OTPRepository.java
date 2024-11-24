package com.shose.shoseshop.repository;

import com.shose.shoseshop.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long>, JpaSpecificationExecutor<OTP> {
    @Query("SELECT o FROM OTP o WHERE o.email = :email ORDER BY o.createdAt DESC")
    Optional<OTP> findTopByEmailOrderByCreateAtDesc(@Param("email") String email);
}

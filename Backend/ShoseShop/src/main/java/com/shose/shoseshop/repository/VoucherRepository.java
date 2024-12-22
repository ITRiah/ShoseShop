package com.shose.shoseshop.repository;

import com.shose.shoseshop.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface VoucherRepository extends JpaRepository<Voucher, Long>, JpaSpecificationExecutor<Voucher> {
    @Query("SELECT v FROM Voucher v WHERE v.expiredTime >= :now")
    List<Voucher> findAllByExpiredTimeAfterOrEqual(@Param("now") Date now);

    List<Voucher> findByIsDeletedFalse();
}

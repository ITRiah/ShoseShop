package com.shose.shoseshop.repository;

import com.shose.shoseshop.entity.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProcedureRepository extends JpaRepository<Procedure, Long>, JpaSpecificationExecutor<Procedure> {
    List<Procedure> findAllByOrderByNameAsc();
}

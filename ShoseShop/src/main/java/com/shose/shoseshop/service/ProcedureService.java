package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.ProcedureRequest;
import com.shose.shoseshop.controller.response.ProcedureResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProcedureService {
    void create(ProcedureRequest procedureRequest);

    void update(ProcedureRequest procedureRequest);

    Page<ProcedureResponse> getAll(Pageable pageable, OrderFilterRequest request);

    void delete(Long id);

    ProcedureResponse getById(Long id);
}

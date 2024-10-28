package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.ProcedureRequest;
import com.shose.shoseshop.controller.response.ProcedureResponse;

import java.util.List;

public interface ProcedureService {
    void create(ProcedureRequest procedureRequest);

    List<ProcedureResponse> getAll();
}

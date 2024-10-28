package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ProcedureRequest;
import com.shose.shoseshop.controller.request.UserRequest;
import com.shose.shoseshop.controller.response.ProcedureResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.ProcedureService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/procedures")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProcedureController {
    ProcedureService procedureService;

    @PostMapping
    public ResponseData<Void> create(@Valid @RequestBody ProcedureRequest procedureRequest) {
        procedureService.create(procedureRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Create procedure is success!");
    }

    @GetMapping
    public ResponseData<List<ProcedureResponse>> getAll() {
        return new ResponseData<>(procedureService.getAll());
    }
}

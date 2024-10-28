package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.ProcedureRequest;
import com.shose.shoseshop.controller.response.ProcedureResponse;
import com.shose.shoseshop.entity.Procedure;
import com.shose.shoseshop.repository.ProcedureRepository;
import com.shose.shoseshop.service.ProcedureService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProcedureServiceImpl implements ProcedureService {
    ProcedureRepository procedureRepository;

    @Override
    public void create(ProcedureRequest procedureRequest) {
        procedureRepository.save(new Procedure(procedureRequest.getName()));
    }

    @Override
    public List<ProcedureResponse> getAll() {
        List<Procedure> procedures = procedureRepository.findAllByOrderByNameAsc();
        return procedures.stream()
                .map(procedure -> new ModelMapper().map(procedure, ProcedureResponse.class))
                .collect(Collectors.toList());
    }
}

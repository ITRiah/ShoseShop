package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.ProcedureRequest;
import com.shose.shoseshop.controller.response.ProcedureResponse;
import com.shose.shoseshop.entity.*;
import com.shose.shoseshop.repository.ProcedureRepository;
import com.shose.shoseshop.repository.ProductDetailRepository;
import com.shose.shoseshop.repository.ProductRepository;
import com.shose.shoseshop.service.ProcedureService;
import com.shose.shoseshop.specification.ProcedureSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProcedureServiceImpl implements ProcedureService {
    ProcedureRepository procedureRepository;
    ProductRepository productRepository;
    ProductDetailRepository productDetailRepository;
    ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(ProcedureRequest procedureRequest) {
        procedureRepository.save(modelMapper.map(procedureRequest, Procedure.class));
    }

    @Override
    @Transactional
    public void update(ProcedureRequest procedureRequest) {
        Procedure procedure = procedureRepository.findById(procedureRequest.getId()).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(procedureRequest, procedure);
        procedureRepository.save(procedure);
    }

    @Override
    public Page<ProcedureResponse> getAll(Pageable pageable, OrderFilterRequest request) {
        Specification<Procedure> spec = ProcedureSpecification.generateFilter(request);
        Page<Procedure> procedurePage = procedureRepository.findAll(spec, pageable);
        return procedurePage.map(procedure -> modelMapper.map(procedure, ProcedureResponse.class));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Procedure procedure = procedureRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<Product> products = productRepository.findByProcedureId(id);
        List<ProductDetail> productDetailList = products.stream()
                .flatMap(product -> product.getProductDetailResponseList().stream())
                .toList();
        productDetailList.forEach(BaseEntity::markAsDelete);
        products.forEach(BaseEntity::markAsDelete);
        procedure.markAsDelete();
        productDetailRepository.saveAll(productDetailList);
        productRepository.saveAll(products);
        procedureRepository.save(procedure);
    }

    @Override
    public ProcedureResponse getById(Long id) {
        Procedure procedure = procedureRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(procedure, ProcedureResponse.class);
    }
}

package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.ProcedureRequest;
import com.shose.shoseshop.controller.response.ProcedureResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.entity.Procedure_;
import com.shose.shoseshop.service.ProcedureService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/procedures")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProcedureController {
    ProcedureService procedureService;

    @PostMapping
    public ResponseData<Void> create(@Valid @RequestBody ProcedureRequest procedureRequest) {
        procedureService.create(procedureRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Nhà sản xuất đã được thêm mới!");
    }

    @PutMapping
    public ResponseData<Void> update(@Valid @RequestBody ProcedureRequest procedureRequest) {
        procedureService.update(procedureRequest);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Cập nhật nhà sản xuất thành công!");
    }

    @PostMapping("/search")
    public ResponseData<ProcedureResponse> getAll(@PageableDefault(size = 10)
                                                 @SortDefault.SortDefaults({@SortDefault(sort = Procedure_.CREATED_AT, direction = Sort.Direction.DESC)})
                                                 Pageable pageable,
                                                 @RequestBody(required = false) OrderFilterRequest request) {
        int page = (request != null && request.getPage() != null) ? request.getPage() : pageable.getPageNumber();
        int size = (request != null && request.getSize() != null) ? request.getSize() : pageable.getPageSize();
        Pageable customPageable = (page == pageable.getPageNumber() && size == pageable.getPageSize())
                ? pageable
                : PageRequest.of(page, size, pageable.getSort());
        return new ResponseData<>(procedureService.getAll(customPageable, request));
    }

    @DeleteMapping
    public ResponseData<Void> delete(@RequestParam Long id) {
        procedureService.delete(id);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Nhà sản xuất đã bị xóa bỏ!");
    }

    @GetMapping("/{id}")
    public ResponseData<ProcedureResponse> getById(@PathVariable Long id) {
        return new ResponseData<>(procedureService.getById(id));
    }
}

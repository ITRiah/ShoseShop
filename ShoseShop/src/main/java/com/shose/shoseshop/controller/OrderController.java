package com.shose.shoseshop.controller;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.OrderRequest;
import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.controller.response.OrderResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.controller.response.UserResponse;
import com.shose.shoseshop.entity.Order_;
import com.shose.shoseshop.entity.User_;
import com.shose.shoseshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderController {

    OrderService orderService;

    @PostMapping
    public ResponseData<Void> create(@Valid @RequestBody OrderRequest orderRequest) {
        orderService.create(orderRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Create order is success!");
    }

    @PutMapping
    public ResponseData<Void> updateStatus(@RequestParam("id") Long id, @RequestParam("status") OrderStatus status) {
        orderService.update(id, status);
        return new ResponseData<>(HttpStatus.CREATED, "Update order is success!");
    }

    @GetMapping
    public ResponseData<OrderResponse> getAll(@PageableDefault(size = 10)
                                             @SortDefault.SortDefaults({@SortDefault(sort = Order_.CREATED_AT, direction = Sort.Direction.DESC)})
                                             Pageable pageable,
                                             @RequestBody(required = false) OrderFilterRequest request) {
        return new ResponseData<>(orderService.getAll(pageable, request));
    }
}

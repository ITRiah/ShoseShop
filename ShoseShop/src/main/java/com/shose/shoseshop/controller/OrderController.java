package com.shose.shoseshop.controller;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.request.OrderRequest;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/procedures")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderController {

    OrderService orderService;

    @PostMapping
    public ResponseData<Void> create(@Valid @RequestBody OrderRequest orderRequest) {
        orderService.create(orderRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Create order is success!");
    }

    @PatchMapping
    public ResponseData<Void> updateStatus(@RequestParam("id") Long id, @RequestParam("status") OrderStatus status) {
        orderService.update(id, status);
        return new ResponseData<>(HttpStatus.CREATED, "Create order is success!");
    }
}

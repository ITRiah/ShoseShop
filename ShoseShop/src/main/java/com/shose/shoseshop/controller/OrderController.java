package com.shose.shoseshop.controller;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.OrderRequest;
import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.controller.response.*;
import com.shose.shoseshop.entity.Order_;
import com.shose.shoseshop.entity.User_;
import com.shose.shoseshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
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
import java.util.List;

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

    @PostMapping("/search")
    public ResponseData<OrderResponse> getAll(@PageableDefault(size = 10)
                                             @SortDefault.SortDefaults({@SortDefault(sort = Order_.CREATED_AT, direction = Sort.Direction.DESC)})
                                             Pageable pageable,
                                             @RequestBody(required = false) OrderFilterRequest request) {
        return new ResponseData<>(orderService.getAll(pageable, request));
    }

    @GetMapping("/statistic")
    public ResponseData<List<StatisticResponse>> getAll(@RequestParam Long year) {
        return new ResponseData<>(orderService.statistic(year));
    }

    @GetMapping("/product-sales")
    public List<ProductStatisticResponse> getProductSalesStatistics(
            @RequestParam("year") Long year,
            @RequestParam("month") Long month) {
        return orderService.findProductSalesStatistic(year, month);
    }
}

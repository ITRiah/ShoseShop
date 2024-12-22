package com.shose.shoseshop.controller;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.constant.PaymentStatus;
import com.shose.shoseshop.controller.request.OrderFilterRequest;
import com.shose.shoseshop.controller.request.OrderRequest;
import com.shose.shoseshop.controller.response.OrderResponse;
import com.shose.shoseshop.controller.response.ProductStatisticResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.controller.response.StatisticResponse;
import com.shose.shoseshop.entity.Order_;
import com.shose.shoseshop.service.OrderService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderController {

    OrderService orderService;

    @PostMapping
    public ResponseData<Long> create(@Valid @RequestBody OrderRequest orderRequest) {
        return new ResponseData<>(HttpStatus.CREATED,"Bạn đã đặt hàng thành công!", orderService.create(orderRequest));
    }

    @PutMapping
    public ResponseData<Void> updateStatus(@RequestParam("id") Long id, @RequestParam("status") OrderStatus status, @RequestParam("paymentStatus") PaymentStatus paymentStatus) {
        orderService.update(id, status, paymentStatus);
        return new ResponseData<>(HttpStatus.CREATED, "Trạng thái đơn hàng đã được cập nhật!");
    }

    @PostMapping("/search")
    public ResponseData<OrderResponse> getAll(@PageableDefault(size = 10)
                                             @SortDefault.SortDefaults({@SortDefault(sort = Order_.CREATED_AT, direction = Sort.Direction.DESC)})
                                             Pageable pageable,
                                             @RequestBody(required = false) OrderFilterRequest request) {
        int page = (request != null && request.getPage() != null) ? request.getPage() : pageable.getPageNumber();
        int size = (request != null && request.getSize() != null) ? request.getSize() : pageable.getPageSize();
        Pageable customPageable = (page == pageable.getPageNumber() && size == pageable.getPageSize())
                ? pageable
                : PageRequest.of(page, size, pageable.getSort());
        return new ResponseData<>(orderService.getAll(customPageable, request));
    }

    @GetMapping("/statistic")
    public ResponseData<List<StatisticResponse>> getAll(@RequestParam Long year) {
        return new ResponseData<>(HttpStatus.OK, "success!", orderService.statistic(year));
    }

    @GetMapping("/product-sales")
    public ResponseData<List<ProductStatisticResponse>> getProductSalesStatistics(
            @RequestParam("year") Long year,
            @RequestParam("month") Long month) {
        return new ResponseData<>(HttpStatus.OK, "success!", orderService.findProductSalesStatistic(month, year));
    }

    @GetMapping("/{id}")
    public ResponseData<OrderResponse> getById(@PathVariable Long id) {
        return new ResponseData<>(orderService.getById(id));
    }

    @GetMapping("/users")
    public ResponseData<List<OrderResponse>> getByUser() {
        return new ResponseData<>(orderService.getByUser());
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseData<Void> cancelOrder(@PathVariable Long id, @RequestParam String reason) {
        orderService.cancelOrder(id, reason);
        return new ResponseData<>(HttpStatus.NO_CONTENT,"Đơn hàng đã được hủy!");
    }

    @GetMapping("/{orderId}/download")
    public byte[] downloadInvoice(@PathVariable Long orderId) {
        return orderService.exportOrder(orderId);
    }
}

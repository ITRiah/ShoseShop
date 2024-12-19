package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.response.CartResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.entity.CartDetail;
import com.shose.shoseshop.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CartController {
    CartService cartService;

    @PutMapping
    public ResponseData<Void> addtoCart(@RequestParam Long productDetailId, @RequestParam Long quantity,  @RequestParam Long actionType) {
        cartService.addToCart(productDetailId, quantity, actionType);
        return new ResponseData<>(HttpStatus.CREATED, "Thêm sản phẩm vào giỏ hàng thành công!");
    }

    @GetMapping
    public ResponseData<CartResponse> getCart() {
        return new ResponseData<>(cartService.getCart());
    }

    @DeleteMapping("/cart-details")
    public ResponseData<Void> deleteCartDetails(@RequestParam Set<Long> ids) {
        cartService.deleteCartDetails(ids);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Bạn đã xóa sản phẩm khỏi giỏ hàng thành công!");
    }
}

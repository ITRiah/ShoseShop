package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.response.CartResponse;
import com.shose.shoseshop.entity.User;

import java.util.Set;

public interface CartService {
    void create(User user);

    void addToCart(Long productDetailId, Long quantity, Long actionType);

    CartResponse getCart();

    void deleteCartDetails(Set<Long> ids);
}

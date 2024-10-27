package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.entity.Cart;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.repository.CartRepository;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Override
    public void create(User user) {
        cartRepository.save(new Cart(user));
    }
}

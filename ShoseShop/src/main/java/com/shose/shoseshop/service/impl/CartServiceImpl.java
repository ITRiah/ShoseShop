package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.configuration.CustomUserDetails;
import com.shose.shoseshop.controller.request.ProductDetailRequest;
import com.shose.shoseshop.controller.response.CartResponse;
import com.shose.shoseshop.entity.*;
import com.shose.shoseshop.repository.*;
import com.shose.shoseshop.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductDetailRepository productDetailRepository;
    private final UserRepository userRepository;
    private final CartDetailRepository cartDetailRepository;
    ProductRepository productRepository;

    @Override
    public void create(User user) {
        cartRepository.save(new Cart(user));
    }

    @Override
    public void addToCart(Long productDetailId, Long quantity) {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(loginUser.getUsername()).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByUser_Id(user.getId()).orElseThrow(EntityNotFoundException::new);
        ProductDetail productDetail = productDetailRepository.findById(productDetailId).orElseThrow(EntityNotFoundException::new);
        CartDetail cartDetail= new CartDetail();
        cartDetail.setProductDetail(productDetail);
        cartDetail.setQuantity(quantity);
        cartDetail.setCartId(cart.getId());
        cartDetail.setProductName(productDetail.getProduct().getName());
        List<CartDetail> cartDetails = cart.getCartDetails();
        cartDetails.add(cartDetail);
        cartDetailRepository.save(cartDetail);
        cartRepository.save(cart);
    }

    @Override
    public CartResponse getCart() {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(loginUser.getUsername()).orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByUser_Id(user.getId()).orElseThrow(EntityNotFoundException::new);
        return new ModelMapper().map(cart, CartResponse.class);
    }

    @Override
    public void deleteCartDetails(Set<Long> ids) {
        List<CartDetail> cartDetails = cartDetailRepository.findByIdIn(ids);
        if (!CollectionUtils.isEmpty(cartDetails)) {
            cartDetails.forEach(BaseEntity::markAsDelete);
        }
        cartDetailRepository.saveAll(cartDetails);
    }
}

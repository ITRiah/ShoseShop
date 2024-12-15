package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.ChangePasswordRequest;
import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.controller.request.UserRequest;
import com.shose.shoseshop.controller.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void create(UserRequest userRequest);

    void deleteUser(Long id);

    void forgotPassword(String email);

    void updatePassword(ChangePasswordRequest request);

    Page<UserResponse> getAll(Pageable pageable, UserFilterRequest request);

    UserResponse getById(Long id);
    void update(UserRequest UserRequest);
    UserResponse getLoginUser();
}

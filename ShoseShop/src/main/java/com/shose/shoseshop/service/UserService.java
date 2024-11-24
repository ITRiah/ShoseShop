package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.controller.request.UserRequest;
import com.shose.shoseshop.controller.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
//    Page<UserDTO> listUser(Pageable pageable, UserFilterForm searchDTO);

    void create(UserRequest userRequest);

//    UserResponse getDetailsCurrentUser(String userName);
//
//    UserResponse getUser(Long id);
//
//    UpdateUserResponse updateUserByID(Long id, UpdateUserRequest updateUserRequest);
    void deleteUser(Long id);

    void forgotPassword(String email);

    void updatePassword(String email, String newPassword, String otp);

    Page<UserResponse> getAll(Pageable pageable, UserFilterRequest request);
}

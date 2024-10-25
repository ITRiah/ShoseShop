package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.error.request.UserRequest;

public interface UserService {
//    Page<UserDTO> listUser(Pageable pageable, UserFilterForm searchDTO);

    void create(UserRequest userRequest);

//    UserResponse getDetailsCurrentUser(String userName);
//
//    UserResponse getUser(Long id);
//
//    UpdateUserResponse updateUserByID(Long id, UpdateUserRequest updateUserRequest);
//    void deleteByUserById(Long id);
}

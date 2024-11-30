package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ProductFilterRequest;
import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.controller.request.UserRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.controller.response.UserResponse;
import com.shose.shoseshop.entity.Product_;
import com.shose.shoseshop.entity.User_;
import com.shose.shoseshop.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {

    UserService userService;

    @PostMapping
    public ResponseData<Void> create(@Valid @RequestBody UserRequest userRequest) {
        userService.create(userRequest);
        return new ResponseData<>(HttpStatus.CREATED, "Create user is success!");
    }

    @PostMapping("forgot-password")
    public ResponseData<Void> forgotPassword(@RequestParam("email") String email) {
        userService.forgotPassword(email);
        return new ResponseData<>(HttpStatus.CREATED, "OTP has been sent to your email!");
    }

    @PutMapping("/password")
    public ResponseData<Void> updatePassword(@RequestParam("email") String email,
                                             @RequestParam("password") String password,
                                             @RequestParam("otp") String otp) {
        userService.updatePassword(email, password, otp);
        return new ResponseData<>(HttpStatus.CREATED, "Your password has been updated!");
    }

    @PutMapping("/block")
    public ResponseData<Void> blockUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return new ResponseData<>(HttpStatus.CREATED, "OTP has been sent to your email!");
    }

    @PostMapping("/search")
    public ResponseData<UserResponse> getAll(@PageableDefault(size = 10)
                                                @SortDefault.SortDefaults({@SortDefault(sort = User_.CREATED_AT, direction = Sort.Direction.DESC)})
                                                Pageable pageable,
                                                @RequestBody(required = false) UserFilterRequest request) {
        return new ResponseData<>(userService.getAll(pageable, request));
    }

    @GetMapping("/{id}")
    public ResponseData<UserResponse> getById(@PathVariable Long id) {
        return new ResponseData<>(userService.getById(id));
    }
}
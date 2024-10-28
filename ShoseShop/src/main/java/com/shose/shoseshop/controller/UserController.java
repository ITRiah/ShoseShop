package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.UserRequest;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    @PatchMapping("/password")
    public ResponseData<Void> forgotPassword(@RequestParam("email") String email,
                                             @RequestParam("password") String password,
                                             @RequestParam("otp") String otp) {
        userService.updatePassword(email, password, otp);
        return new ResponseData<>(HttpStatus.CREATED, "Your password has been updated!");
    }
}
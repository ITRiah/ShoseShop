package com.shose.shoseshop.controller.error;

import com.shose.shoseshop.controller.error.request.UserRequest;
import com.shose.shoseshop.controller.error.response.ResponseData;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
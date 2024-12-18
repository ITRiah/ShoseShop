package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.ChangePasswordRequest;
import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.controller.request.UserRequest;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.controller.response.UserResponse;
import com.shose.shoseshop.entity.User_;
import com.shose.shoseshop.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
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
        return new ResponseData<>(HttpStatus.CREATED, "Đăng kí tài khoản thành công!");
    }

    @PostMapping("forgot-password")
    public ResponseData<Void> forgotPassword(@RequestParam("email") String email) {
        userService.forgotPassword(email);
        return new ResponseData<>(HttpStatus.CREATED, "OTP đã đự gửi đến email của bạn!");
    }

    @PutMapping("/password")
    public ResponseData<Void> updatePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.updatePassword(changePasswordRequest);
        return new ResponseData<>(HttpStatus.OK, "Mật khẩu của bạn đã được cập nhât!");
    }

    @PutMapping("/block")
    public ResponseData<Void> blockUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return new ResponseData<>(HttpStatus.CREATED, "Cập nhật trạng thái tài khoản thành công!");
    }

    @PostMapping("/search")
    public ResponseData<UserResponse> getAll(@PageableDefault(size = 10)
                                                @SortDefault.SortDefaults({@SortDefault(sort = User_.CREATED_AT, direction = Sort.Direction.DESC)})
                                                Pageable pageable,
                                                @RequestBody(required = false) UserFilterRequest request) {
        int page = (request != null && request.getPage() != null) ? request.getPage() : pageable.getPageNumber();
        int size = (request != null && request.getSize() != null) ? request.getSize() : pageable.getPageSize();
        Pageable customPageable = (page == pageable.getPageNumber() && size == pageable.getPageSize())
                ? pageable
                : PageRequest.of(page, size, pageable.getSort());
        return new ResponseData<>(userService.getAll(customPageable, request));
    }

    @GetMapping("/{id}")
    public ResponseData<UserResponse> getById(@PathVariable Long id) {
        return new ResponseData<>(userService.getById(id));
    }

    @PutMapping
    public ResponseData<Void> update(@RequestBody UserRequest userRequest) {
        userService.update(userRequest);
        return new ResponseData<>(HttpStatus.NO_CONTENT, "Cập nhật thông tin cá nhân thành công!");
    }

    @GetMapping
    public ResponseData<UserResponse> getLoginUser() {
        return new ResponseData<>(userService.getLoginUser());
    }
}
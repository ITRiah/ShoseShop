package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.ChangePasswordRequest;
import com.shose.shoseshop.controller.request.UserFilterRequest;
import com.shose.shoseshop.controller.request.UserRequest;
import com.shose.shoseshop.controller.response.ProductResponse;
import com.shose.shoseshop.controller.response.UserResponse;
import com.shose.shoseshop.entity.OTP;
import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.repository.OTPRepository;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.service.CartService;
import com.shose.shoseshop.service.EmailService;
import com.shose.shoseshop.service.OTPService;
import com.shose.shoseshop.service.UserService;
import com.shose.shoseshop.specification.ProductSpecification;
import com.shose.shoseshop.specification.UserSpecification;
import com.shose.shoseshop.util.ObjectUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    BCryptPasswordEncoder passwordEncoder;
    UserRepository userRepository;
    CartService cartService;
    EmailService emailService;
    OTPService otpService;
    ModelMapper modelMapper;
    OTPRepository otpRepository;

    @Override
    public void create(UserRequest userRequest) {
        //create user
        User user = new ModelMapper().map(userRequest, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);

        //create cart
        cartService.create(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (user.getIsDeleted()) {
            user.setIsDeleted(Boolean.FALSE);
        } else {
            user.markAsDelete();
        }
        userRepository.save(user);
    }

    @Override
    public void updatePassword(ChangePasswordRequest request) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (Objects.nonNull(request.getEmail())) {
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(EntityNotFoundException::new);
            userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new EntityNotFoundException("User has email " + request.getEmail() + " does not exists!"));
            OTP otp = otpRepository.findByEmail(request.getEmail()).orElse(null);
            if (otp != null && otp.getOtp().equals(request.getOtp())) {
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("OTP đã hết hạn hoặc sai OTP, hãy check email của bạn!");
            }
        } else {
            UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userRepository.findByUsername(loginUser.getUsername()).orElseThrow(EntityNotFoundException::new);
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Mật khẩu cũ không đúng!");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        }
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable, UserFilterRequest request) {
        Specification<User> specUser = UserSpecification.generateFilter(request);
        Page<User> userPage = userRepository.findAll(specUser, pageable);
        return userPage.map(user -> modelMapper.map(user, UserResponse.class));
    }

    @Override
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public void update(UserRequest userRequest) {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(loginUser.getUsername()).orElseThrow(EntityNotFoundException::new);
        String password = user.getPassword();
        modelMapper.map(userRequest, user);
        user.setPassword(password);
        userRepository.save(user);
    }

    @Override
    public UserResponse getLoginUser() {
        UserDetails loginUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(loginUser.getUsername()).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public void forgotPassword(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        OTP oldOTP = otpRepository.findByEmail(email).orElse(null);
        if (oldOTP == null) {
            OTP otp = otpService.create(email);
            emailService.sendOTP(otp.getOtp(), Set.of(email));
        } else {
            oldOTP.markAsDelete();
            otpRepository.save(oldOTP);
        }
    }


}

package com.shose.shoseshop.service.impl;

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
        user.markAsDelete();
        userRepository.save(user);
    }

    @Override
    public void updatePassword(String email, String newPassword, String otpStr) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        OTP otp = otpRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("OTP is expired!"));;
        if (otpStr.equals(otp.getOtp())) {
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("OTP is invalid, please check your email!");
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
        modelMapper.map(userRequest, user);
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
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        OTP oldOTP = otpRepository.findByEmail(email).orElse(null);
        if (oldOTP == null) {
            OTP otp = otpService.create(email);
            emailService.sendMail("Request to retrieve password!", "Your OTP: " + otp.getOtp(), email);
        } else {
            oldOTP.markAsDelete();
            otpRepository.save(oldOTP);
        }
    }


}

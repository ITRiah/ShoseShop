package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.controller.request.UserRequest;
import com.shose.shoseshop.entity.OTP;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.service.CartService;
import com.shose.shoseshop.service.EmailService;
import com.shose.shoseshop.service.OTPService;
import com.shose.shoseshop.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
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
        OTP otp = otpService.getLastestOTPByEmail(email);
        if (otpStr.equals(otp.getOtp())) {
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Password is invalid, please check your email!");
        }
    }

    @Override
    public void forgotPassword(String email) {
        OTP otp = otpService.create(email);
        emailService.sendMail("Request to retrieve password!", "Your OTP: " + otp.getOtp(), email);
    }
}

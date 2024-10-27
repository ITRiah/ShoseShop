package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.entity.OTP;
import com.shose.shoseshop.repository.OTPRepository;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.service.EmailService;
import com.shose.shoseshop.service.OTPService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OTPServiceImpl implements OTPService {
    OTPRepository otpRepository;
    UserRepository userRepository;
    EmailService emailService;
    SecureRandom RANDOM = new SecureRandom();

    @Override
    public OTP create(String email) {
        userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
        int LENGTH = 6;
        StringBuilder code = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            String CHARACTERS = "0123456789";
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return otpRepository.save(new OTP(code.toString(), email));
    }

    @Override
    public OTP getLastestOTPByEmail(String email) {
        return otpRepository.findTopByEmailOrderByCreateAtDesc(email).orElseThrow(EntityNotFoundException::new);
    }
}

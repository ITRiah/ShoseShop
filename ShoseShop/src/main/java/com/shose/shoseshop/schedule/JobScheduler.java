package com.shose.shoseshop.schedule;

import com.shose.shoseshop.entity.BaseEntity;
import com.shose.shoseshop.entity.OTP;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.entity.Voucher;
import com.shose.shoseshop.repository.OTPRepository;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.repository.VoucherRepository;
import com.shose.shoseshop.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobScheduler {
    private final UserRepository userRepo;
    private final EmailService emailService;
    private final VoucherRepository voucherRepository;
    private final OTPRepository otpRepository;

    @Scheduled(cron = "0 30 8 * * *")
    public void sendMail() {
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        List<User> users = userRepo.searchByBirthDay(day, month);

        for (User user : users) {
            emailService.sendBirthDay(user.getEmail(), user.getUsername());
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateVoucher() {
        List<Voucher> vouchers = voucherRepository.findAllByExpiredTimeAfterOrEqual(new Date());
        if (!CollectionUtils.isEmpty(vouchers)) {
            vouchers.forEach(BaseEntity::markAsDelete);
        }
    }

    @Scheduled(fixedRate = 1000)
    public void markExpiredOtps() {
        Instant now = Instant.now();
        List<OTP> expiredOtps = otpRepository.findAll();
        expiredOtps = expiredOtps.stream()
                .filter(otp -> !otp.getIsDeleted() && otp.getCreatedAt().plusSeconds(60).isBefore(now))
                .toList();
        for (OTP otp : expiredOtps) {
            otp.setIsDeleted(true);
        }
        otpRepository.saveAll(expiredOtps);
    }

    @Scheduled(fixedRate = 86400000)
    public void markExpiredVouchers() {
        Date now = new Date();
        List<Voucher> expiredVouchers = voucherRepository.findAll();
        expiredVouchers = expiredVouchers.stream()
                .filter(voucher -> !voucher.getIsDeleted() &&
                        voucher.getExpiredTime() != null &&
                        voucher.getExpiredTime().before(now))
                .toList();
        for (Voucher voucher : expiredVouchers) {
            voucher.setIsDeleted(true);
        }
        voucherRepository.saveAll(expiredVouchers);
    }

}
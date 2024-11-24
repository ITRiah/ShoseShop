package com.shose.shoseshop.schedule;

import com.shose.shoseshop.entity.BaseEntity;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.entity.Voucher;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.repository.VoucherRepository;
import com.shose.shoseshop.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobScheduler {
    private final UserRepository userRepo;
    private final EmailService emailService;
    private final VoucherRepository voucherRepository;

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
}
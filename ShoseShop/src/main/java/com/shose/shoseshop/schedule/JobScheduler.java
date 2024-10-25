package com.shose.shoseshop.schedule;

import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobScheduler {
    UserRepository userRepo;
    EmailService emailService;

    @Scheduled(cron = "* 10 * * * *")
    public void sendMail() {
        LocalDate date = LocalDate.now();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        List<User> users = userRepo.searchByBirthDay(day, month);

        for (User user : users) {
            emailService.sendBirthDay(user.getEmail(), user.getUsername());
        }
    }
}
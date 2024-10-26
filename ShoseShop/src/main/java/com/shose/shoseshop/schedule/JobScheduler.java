package com.shose.shoseshop.schedule;

import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JobScheduler {
    private final UserRepository userRepo;
    private final EmailService emailService;

//    @Scheduled(cron = "* * * * * *")
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
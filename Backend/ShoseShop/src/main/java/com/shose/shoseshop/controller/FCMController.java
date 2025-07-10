package com.shose.shoseshop.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class FCMController {
    private final Map<String, String> tokens = new ConcurrentHashMap<>();
    private final NotificationService notificationService;

    public FCMController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/save-token")
    public ResponseEntity<?> saveToken(@RequestBody TokenDTO body) {
        String userId = body.getUserId();
        String token = body.getToken();
        if (userId == null || token == null || userId.isEmpty() || token.isEmpty()) {
            System.out.println("userId hoặc token không hợp lệ: userId=" + userId + ", token=" + token);
            return ResponseEntity.badRequest().body("userId hoặc token không hợp lệ");
        }
        tokens.put(userId, token);
        System.out.println("Lưu fcmToken cho userId: " + userId + ", token: " + token);
        return ResponseEntity.ok("Đã lưu token cho userId: " + userId);
    }

    @GetMapping("/send-test/{userId}")
    public String sendTest(@PathVariable String userId) {
        String fcmToken = tokens.get(userId);
        if (fcmToken != null && !fcmToken.isEmpty()) {
            try {
                notificationService.sendToUser(userId, "Order Update", "Your order has been shipped!", fcmToken);
                return "Gửi thông báo thành công đến userId: " + userId;
            } catch (Exception e) {
                System.err.println("Lỗi khi gửi thông báo đến userId " + userId + ": " + e.getMessage());
                return "Lỗi khi gửi thông báo đến userId " + userId + ": " + e.getMessage();
            }
        }
        return "Không tìm thấy token cho userId: " + userId;
    }

    @GetMapping("/statistics")
    public ResponseData<List<Abc>> sendTest(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
            return new ResponseData<>(List.of(new Abc(YearMonth.now(), 10L)));
    }

    public String getToken(String userId) {
        String token = tokens.get(userId);
        System.out.println("Lấy token cho userId: " + userId + ", token: " + (token != null ? token : "null"));
        return token;
    }

    @Data
    @NoArgsConstructor
    public static class TokenDTO {
        private String userId;
        private String token;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Abc {
        @JsonFormat(pattern = "MM/yyyy")
        private YearMonth month;
        private Long total;
    }
}


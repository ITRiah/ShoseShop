package com.shose.shoseshop.controller.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class Notice implements Serializable {
    private String subject; // Tiêu đề thông báo
    private String content; // Nội dung thông báo
    private String image; // URL hình ảnh (nếu có)
    private Map<String, String> data; // Dữ liệu bổ sung
    private List<String> registrationTokens; // Danh sách FCM tokens
}

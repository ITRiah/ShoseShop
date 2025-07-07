package com.shose.shoseshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Abc {

    @GetMapping("/notifications")
    public String notificationsPage() {
        return "notification";
    }
}

package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.request.PaymentDTO;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.OrderService;
import com.shose.shoseshop.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Controller
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final SpringTemplateEngine templateEngine;

    @GetMapping("/vn-pay")
    @ResponseBody
    public ResponseData<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseData<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }

    @GetMapping("/vn-pay-callback")
    public String payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        Long orderId = Long.valueOf(request.getParameter("vnp_TxnRef"));
        if (status.equals("00")) {
            orderService.updatePaymentStatus(orderId);
            return "success";
        } else {
            return "fail";
        }
    }
}

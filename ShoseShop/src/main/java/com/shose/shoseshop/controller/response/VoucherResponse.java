package com.shose.shoseshop.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {
    private Long id;
    private String code;
    private Integer value;
    private Integer quantity;
    private BigDecimal maxMoney;
    private Date expiredTime;
    private String description;
}
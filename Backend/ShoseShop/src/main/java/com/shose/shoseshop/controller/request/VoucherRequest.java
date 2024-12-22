package com.shose.shoseshop.controller.request;

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
public class VoucherRequest {
    private Long id;
    private String code;
    private String img;
    private Integer value;
    private Integer quantity;
    private BigDecimal maxMoney;
    private Date expiredTime;
    private String description;
}

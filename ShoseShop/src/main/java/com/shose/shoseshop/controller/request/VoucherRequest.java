package com.shose.shoseshop.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherRequest {
    private String code;
    private Integer value;
    private Integer quantity;
    private BigDecimal maxMoney;
    private LocalDateTime expiredTime;
    private String description;
    private Set<Long> userIds;
    private Boolean isDeleted = Boolean.FALSE;
}

package com.shose.shoseshop.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticResponse {
    private int year;
    private int month;
    private BigDecimal totalRevenue;
}

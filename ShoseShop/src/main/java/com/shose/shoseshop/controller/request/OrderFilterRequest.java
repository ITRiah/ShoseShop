package com.shose.shoseshop.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilterRequest {
    private Long id;
    private Date dateFrom;
    private Date dateTo;
    private String fullName;
    private String name;
}
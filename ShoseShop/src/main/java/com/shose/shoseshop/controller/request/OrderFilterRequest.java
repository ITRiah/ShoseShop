package com.shose.shoseshop.controller.request;

import com.shose.shoseshop.constant.OrderStatus;
import com.shose.shoseshop.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilterRequest extends PageRequest {
    private Long id;
    private Date dateFrom;
    private Date dateTo;
    private String fullName;
    private String name;
    private Role role;
    private OrderStatus status;
}

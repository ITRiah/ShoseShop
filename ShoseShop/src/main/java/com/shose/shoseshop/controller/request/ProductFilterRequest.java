package com.shose.shoseshop.controller.request;

import com.shose.shoseshop.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequest extends PageRequest{
    private Set<Long> procedureIds;
    private Set<Long> categoryIds;
    private String name;
    private BigDecimal priceBigger;
    private BigDecimal priceLower;
    Role role;
}

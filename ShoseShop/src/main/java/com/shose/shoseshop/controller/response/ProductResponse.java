package com.shose.shoseshop.controller.response;

import com.shose.shoseshop.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse extends BaseEntity {
    private Long id;
    private ProcedureResponse procedure;
    private CategoryResponse category;
    private String name;
    private String description;
    private BigDecimal priceRange;
    private Float star;
    private String img;
    List<ProductDetailResponse> productDetailResponseList;
}

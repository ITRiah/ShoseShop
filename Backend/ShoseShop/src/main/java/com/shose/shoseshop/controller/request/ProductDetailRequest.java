package com.shose.shoseshop.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailRequest {
    private Long id;
    private String color;
    private String size;
    private Integer quantity;
    private String img;
    private Long productId;
    private BigDecimal price;

    @JsonIgnore
    private MultipartFile file;
}


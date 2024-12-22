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
public class ProductRequest {
    private Long id;
    private Long procedure;
    private Long category;
    private String name;
    private String description;
    private BigDecimal priceRange;
    private String img;

    @JsonIgnore
    private MultipartFile file;
}

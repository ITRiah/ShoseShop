package com.shose.shoseshop.controller.response;

import com.shose.shoseshop.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse extends BaseEntity {
    private Long id;
    private String name;
    private String img;
}

package com.shose.shoseshop.controller.response;

import com.shose.shoseshop.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureResponse extends BaseEntity {
    private Long id;
    private String name;
    private String img;
}

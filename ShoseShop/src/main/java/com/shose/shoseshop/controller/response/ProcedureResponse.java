package com.shose.shoseshop.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureResponse {
    private Long id;
    private String name;
    private Boolean isDeleted;
}

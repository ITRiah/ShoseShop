package com.shose.shoseshop.controller.request;

import com.shose.shoseshop.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterRequest extends PageRequest {
    private String email;
    private Role role;
}

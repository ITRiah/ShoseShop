package com.shose.shoseshop.controller.response;

import com.shose.shoseshop.constant.Role;
import com.shose.shoseshop.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends BaseEntity {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private Date birthday;
    private String address;
    private Role role;
}

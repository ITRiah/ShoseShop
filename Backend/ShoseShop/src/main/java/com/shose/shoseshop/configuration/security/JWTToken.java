package com.shose.shoseshop.configuration.security;

import com.shose.shoseshop.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Object to return as body in JWT Authentication.
 */
@Getter
@Setter
@AllArgsConstructor
public class JWTToken {
    private String accessToken;
    private String role;
}

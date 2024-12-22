package com.shose.shoseshop.controller;

import com.shose.shoseshop.configuration.security.JWTToken;
import com.shose.shoseshop.configuration.security.TokenProvider;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.controller.vm.LoginVM;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {
    TokenProvider tokenProvider;
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseData<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getUsername(),
                loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication);
        return new ResponseData<>(new JWTToken(jwt, authentication.getAuthorities().toString()));
    }
}

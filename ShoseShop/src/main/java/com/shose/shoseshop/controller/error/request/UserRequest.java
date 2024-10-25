package com.shose.shoseshop.controller.error.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shose.shoseshop.constant.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
public class UserRequest {
    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @JsonIgnore
    private MultipartFile avatar;

    private Date birthday;

    @Size(min = 1, max = 50)
    private String address;
    private Role role = Role.USER;
}

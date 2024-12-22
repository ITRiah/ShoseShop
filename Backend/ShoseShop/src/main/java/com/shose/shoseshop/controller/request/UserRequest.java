package com.shose.shoseshop.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shose.shoseshop.constant.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    @JsonIgnore
    private MultipartFile file;
    private Date birthday;
    private String address;
    private Role role;
}

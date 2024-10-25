package com.shose.shoseshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shose.shoseshop.constant.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Where;

import java.util.Date;

@Entity
@Table(name = "user")
@Getter
@Setter
@Where(clause = "is_deleted = false")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @Column(name = "password_hash", length = 60)
    @NotNull
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true, nullable = false)
    private String email;

    @Size(max = 50)
    @Column(name = "avatar", length = 50)
    private String avatar;

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "address")
    @Size(min = 1, max = 50)
    private String address;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
}


package com.shose.shoseshop.configuration;

import com.shose.shoseshop.constant.Role;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
@Data
@Accessors(chain = true)
public class CustomUserDetails implements UserDetails {

    private Long userId;
    private String email;
    private Role role;
    private boolean isAuthorizeAdmin;

    public CustomUserDetails(Long userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public CustomUserDetails(Long userId, String email, Role role, boolean isAuthorizeAdmin) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.isAuthorizeAdmin = isAuthorizeAdmin;
    }

    public CustomUserDetails() {

    }

    @Override
    public String getPassword() {
        throw new RuntimeException("Calls to UserDetails.getPassword() are forbidden");
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.emptySet();
    }

    public boolean isAuthorizeAdmin() { return isAuthorizeAdmin; }
}


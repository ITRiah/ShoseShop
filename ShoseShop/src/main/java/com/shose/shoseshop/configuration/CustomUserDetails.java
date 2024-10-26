package com.shose.shoseshop.configuration;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Data
@Accessors(chain = true)
public class CustomUserDetails implements UserDetails {

    private String userId;
    private String email;
    private String role;
    private Set<Long> departmentIds;
    private boolean isAuthorizeAdmin;

    public CustomUserDetails(String userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.departmentIds = Collections.emptySet();
    }

    public CustomUserDetails(String userId, String email, String role, Set<Long> departmentIds) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.departmentIds = departmentIds;
    }

    public CustomUserDetails(String userId, String email, String role, Set<Long> departmentIds, boolean isAuthorizeAdmin) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.departmentIds = departmentIds;
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


package com.project.lettertome_be.domain.user.jwt.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final String email;
    private final String password;

    public CustomUserDetails(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); //빈 collection을 반환
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() { //이메일을 사용자 이름으로 사용하는 경우, getUsername 메서드에서 이메일을 반환하도록 해야함.
        return email;
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
    public boolean isEnabled() {
        return true;
    }
}

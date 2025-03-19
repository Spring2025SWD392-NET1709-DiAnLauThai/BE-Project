package com.be.back_end.service.AccountService;

import java.util.Collection;
import java.util.Collections;

import com.be.back_end.enums.ActivationEnums;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.be.back_end.model.Account;

public class AccountDetailsImpl implements UserDetails{

    private String id;
    private String email;
    private String password;
    private ActivationEnums status;
    private Collection<? extends GrantedAuthority> authorities;

    public AccountDetailsImpl(String id, String email, String password, ActivationEnums status, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.status = status;
        this.authorities = authorities;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
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
        return this.status == ActivationEnums.ACTIVE;
    }

    public String getId() {
        return id;
    }

    public static AccountDetailsImpl build(Account account) {
        GrantedAuthority authority = new SimpleGrantedAuthority(account.getRole().name());
        return new AccountDetailsImpl(
            account.getId(),
            account.getEmail(),
            account.getPassword(),
                account.getStatus(),
                Collections.singletonList(authority)
        );
    }

}

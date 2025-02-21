package com.be.back_end.utils;

import com.be.back_end.model.Account;
import com.be.back_end.security.jwt.JwtUtils;
import com.be.back_end.service.AccountService.AccountDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccountUtils {

    private final JwtUtils jwtutils ;

    public AccountUtils(JwtUtils jwtutils) {
        this.jwtutils = jwtutils;
    }

    public Account getCurrentAccount() {
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public String getCurrentJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String) {
            String token = authentication.getCredentials().toString();
                return token;
        }
        return null;
    }

}

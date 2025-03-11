package com.be.back_end.utils;

import com.be.back_end.model.Account;
import com.be.back_end.repository.AccountRepository;
import com.be.back_end.config.jwt.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccountUtils {

    private final JwtUtils jwtutils ;
    private final AccountRepository accountRepository;

    public AccountUtils(JwtUtils jwtutils, AccountRepository accountRepository) {
        this.jwtutils = jwtutils;
        this.accountRepository = accountRepository;
    }

    public Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getCredentials() == null) {
            throw new IllegalStateException("No authentication found");
        }
        String token = authentication.getCredentials().toString();
        String accountId = jwtutils.getUserIdFromJwtToken(token);
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalStateException("Account not found for ID: " + accountId));
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

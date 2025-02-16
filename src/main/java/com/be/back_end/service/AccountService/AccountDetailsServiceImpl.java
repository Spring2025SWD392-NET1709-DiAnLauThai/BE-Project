package com.be.back_end.service.AccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.be.back_end.model.Account;
import com.be.back_end.repository.AccountRepository;

@Service
public class AccountDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private AccountRepository accountRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + username));
        return AccountDetailsImpl.build(account);
    }
}
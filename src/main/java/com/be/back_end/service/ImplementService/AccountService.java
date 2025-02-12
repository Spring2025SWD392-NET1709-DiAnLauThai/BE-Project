package com.be.back_end.service.ImplementService;


import com.be.back_end.repository.AccountRepository;
import com.be.back_end.service.InterfaceService.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    /*
    @Override
    public List<String> getAll() {
        return List.of();
    }

    @Override
    public String getById(int id) {
        return null;
    }

    @Override
    public List<String> getByName(String name) {
        return List.of();
    }

    @Override
    public String save(String user) {
        return null;
    }

    @Override
    public void removeById(int id) {

    }

     */
}

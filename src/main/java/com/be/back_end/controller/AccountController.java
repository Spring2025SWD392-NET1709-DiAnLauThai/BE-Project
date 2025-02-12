package com.be.back_end.controller;

import com.be.back_end.service.ImplementService.AccountService;
import com.be.back_end.service.InterfaceService.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountController {

    private final IAccountService userService;

    @Autowired
    public AccountController(AccountService userService) {
        this.userService = userService;
    }
}

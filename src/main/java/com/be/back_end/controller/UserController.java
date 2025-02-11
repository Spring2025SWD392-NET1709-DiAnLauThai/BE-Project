package com.be.back_end.controller;

import com.be.back_end.service.ImplementService.TshirtService;
import com.be.back_end.service.ImplementService.UserService;
import com.be.back_end.service.InterfaceService.ITshirtService;
import com.be.back_end.service.InterfaceService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
}

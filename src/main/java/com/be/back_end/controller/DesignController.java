package com.be.back_end.controller;

import com.be.back_end.service.DesignService.IDesignService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/design")
public class DesignController {
    private final IDesignService designService;

    public DesignController(IDesignService designService) {
        this.designService = designService;
    }

}

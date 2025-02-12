package com.be.back_end.controller;

import com.be.back_end.service.ImplementService.MaterialService;
import com.be.back_end.service.ImplementService.TshirtService;
import com.be.back_end.service.InterfaceService.IMaterialService;
import com.be.back_end.service.InterfaceService.ITshirtService;
import org.springframework.beans.factory.annotation.Autowired;

public class MaterialController {

    private final IMaterialService materialService;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }
}

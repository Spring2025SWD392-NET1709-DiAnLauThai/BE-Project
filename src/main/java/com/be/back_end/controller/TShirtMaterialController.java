package com.be.back_end.controller;

import com.be.back_end.dto.TshirtMaterialDTO;
import com.be.back_end.service.TShirtMaterialService.ITshirtMaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tshirtmaterials")
public class TShirtMaterialController {
    private final ITshirtMaterialService tshirtMaterialService;

    public TShirtMaterialController(ITshirtMaterialService tshirtMaterialService) {
        this.tshirtMaterialService = tshirtMaterialService;
    }

    @PostMapping
    public ResponseEntity<String> addTShirtMaterial(@RequestBody TshirtMaterialDTO dto) {
        return tshirtMaterialService.createTShirtMaterial(dto) ?
                ResponseEntity.ok("TShirt material added") :
                ResponseEntity.badRequest().body("Failed to add TShirt material");
    }
}
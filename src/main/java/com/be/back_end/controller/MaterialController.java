package com.be.back_end.controller;

import com.be.back_end.dto.MaterialDTO;
import com.be.back_end.model.Material;
import com.be.back_end.service.ImplementService.MaterialService;
import com.be.back_end.service.MaterialService.IMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material")
public class MaterialController {
    private final com.be.back_end.service.MaterialService.IMaterialService materialService;

    public MaterialController(IMaterialService materialService) {
        this.materialService = materialService;
    }

    @PostMapping
    public String addMaterial(@RequestBody MaterialDTO materialDTO) {
        return materialService.createMaterial(materialDTO) ? "Material added" : "Failed to add material";
    }

    @GetMapping
    public List<Material> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/{id}")
    public Material getMaterialById(@PathVariable String id) {
        return materialService.getMaterialById(id);
    }

    @PutMapping("/{id}")
    public Material updateMaterial(@PathVariable String id, @RequestBody MaterialDTO materialDTO) {
        return materialService.updateMaterial(id, materialDTO);
    }

    @DeleteMapping("/{id}")
    public String deleteMaterial(@PathVariable String id) {
        return materialService.deleteMaterial(id) ? "Material deleted" : "Failed to delete material";
    }
}

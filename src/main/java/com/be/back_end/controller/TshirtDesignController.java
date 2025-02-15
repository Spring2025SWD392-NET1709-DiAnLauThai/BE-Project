package com.be.back_end.controller;

import com.be.back_end.dto.TshirtDesignDTO;
import com.be.back_end.service.TshirtDesignService.TshirtDesignService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class TshirtDesignController {

    private final TshirtDesignService tshirtDesignService;

    public TshirtDesignController(TshirtDesignService tshirttshirtDesignService) {
        this.tshirtDesignService = tshirttshirtDesignService;
    }

    @GetMapping
    public ResponseEntity<List<TshirtDesignDTO>> getAllDesigns() {
        List<TshirtDesignDTO> designs = tshirtDesignService.getAllTshirtDesigns();
        if (designs.isEmpty()) {
            System.out.println("No TShirt designs found.");
        }
        return ResponseEntity.ok(designs);
    }

    @PostMapping
    public ResponseEntity<TshirtDesignDTO> createTshirtDesign(@RequestBody TshirtDesignDTO TshirtDesignDTO) {
        TshirtDesignDTO createdDesign = tshirtDesignService.createTshirtDesign(TshirtDesignDTO);
        System.out.println("Tshirt Design created successfully.");
        return ResponseEntity.ok(createdDesign);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTshirtDesignById(@PathVariable String id) {
        TshirtDesignDTO design = tshirtDesignService.getTshirtDesignById(id);
        if (design == null) {
            return ResponseEntity.badRequest().body("Tshirt design not found with ID: " + id);
        }
        return ResponseEntity.ok(design);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTshirtDesign(@PathVariable String id, @RequestBody TshirtDesignDTO TshirtDesignDTO) {
        boolean updated = tshirtDesignService.updateTshirtDesign(id, TshirtDesignDTO);
        if (!updated) {
            return ResponseEntity.badRequest().body("Failed to update. Tshirt Design not found with ID: " + id);
        }
        return ResponseEntity.ok("Tshirt Design updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTshirtDesign(@PathVariable String id) {
        boolean deleted = tshirtDesignService.deleteTshirtDesign(id);
        if (!deleted) {
            return ResponseEntity.badRequest().body("Failed to delete. Tshirt Design not found with ID: " + id);
        }
        return ResponseEntity.ok("Tshirt Design deleted successfully.");
    }
}

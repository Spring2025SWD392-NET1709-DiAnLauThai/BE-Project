package com.be.back_end.controller;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.DesignDTO;
import com.be.back_end.model.Designs;
import com.be.back_end.service.DesignService.IDesignService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/design")
public class DesignController {
    private final IDesignService designService;

    public DesignController(IDesignService designService) {
        this.designService = designService;
    }

    @GetMapping
    public ResponseEntity<List<DesignDTO>> getAllDesigns() {
        List<DesignDTO> designs = designService.getAll();
        if (designs.isEmpty()) {
            System.out.println("No designs found.");
        }
        return ResponseEntity.ok(designs);
    }

    @PostMapping
    public ResponseEntity<DesignDTO> createAccount(@RequestBody DesignDTO designDTO) {
        DesignDTO createdDesign = designService.create(designDTO);
        System.out.println("Design created successfully.");
        return ResponseEntity.ok(createdDesign);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable String id) {
        DesignDTO design = designService.getById(id);
        if (design == null) {
            return ResponseEntity.badRequest().body("Design not found with ID: " + id);
        }
        return ResponseEntity.ok(design);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable String id, @RequestBody DesignDTO designDTO) {
        boolean updated = designService.update(id, designDTO);
        if (!updated) {
            return ResponseEntity.badRequest().body("Failed to update. Account not found with ID: " + id);
        }
        return ResponseEntity.ok("Account updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable String id) {
        boolean deleted = designService.removeById(id);
        if (!deleted) {
            return ResponseEntity.badRequest().body("Failed to delete. Design not found with ID: " + id);
        }
        return ResponseEntity.ok("Design deleted successfully.");
    }
}

package com.be.back_end.controller;

import com.be.back_end.dto.SizeDTO;
import com.be.back_end.model.Size;
import com.be.back_end.service.SizeService.ISizeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/size")
public class SizeController {
    private final ISizeService sizeService;

    public SizeController(ISizeService sizeService) {
        this.sizeService = sizeService;
    }
    @PostMapping
    public ResponseEntity<String> addSize(@RequestBody SizeDTO size) {
        if (sizeService.createSize(size)) {
            return ResponseEntity.ok("Size added successfully");
        }
        return ResponseEntity.badRequest().body("Size failed to add");
    }

    @GetMapping
    public ResponseEntity<List<Size>> getAllSizes() {
        return ResponseEntity.ok(sizeService.getAllSizes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Size> getSizeById(@PathVariable int id) {
        Size size = sizeService.getSizeById(id);
        if (size != null) {
            return ResponseEntity.ok(size);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSize(@PathVariable int id, @RequestBody SizeDTO size) {
        Size updatedSize = sizeService.updateUser(id, size);
        if (updatedSize != null) {
            return ResponseEntity.ok("Size updated successfully");
        }
        return ResponseEntity.badRequest().body("Size update failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSize(@PathVariable int id) {
        if (sizeService.deleteUser(id)) {
            return ResponseEntity.ok("Size deleted successfully");
        }
        return ResponseEntity.badRequest().body("Size deletion failed");
    }
}

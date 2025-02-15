package com.be.back_end.controller;

import com.be.back_end.dto.ColorDTO;
import com.be.back_end.model.Color;
import com.be.back_end.service.ColorService.IColorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/color")
public class ColorController {
    private final IColorService colorService;

    public ColorController(IColorService colorService) {
        this.colorService = colorService;
    }

    @PostMapping
    public ResponseEntity<String> addColor(@RequestBody ColorDTO colorDTO) {
        if (colorService.createColor(colorDTO)) {
            return ResponseEntity.ok("Color added successfully");
        }
        return ResponseEntity.badRequest().body("Color failed to add");
    }

    @GetMapping
    public ResponseEntity<List<Color>> getAllColors() {
        return ResponseEntity.ok(colorService.getAllColors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Color> getColorById(@PathVariable String id) {
        Color color = colorService.getColorById(id);
        if (color != null) {
            return ResponseEntity.ok(color);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateColor(@PathVariable String id, @RequestBody ColorDTO colorDTO) {
        Color updatedColor = colorService.updateColor(id, colorDTO);
        if (updatedColor != null) {
            return ResponseEntity.ok("Color updated successfully");
        }
        return ResponseEntity.badRequest().body("Color update failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteColor(@PathVariable String id) {
        if (colorService.deleteColor(id)) {
            return ResponseEntity.ok("Color deleted successfully");
        }
        return ResponseEntity.badRequest().body("Color deletion failed");
    }
}

package com.be.back_end.controller;

import com.be.back_end.dto.request.ColorCreateRequest;
import com.be.back_end.dto.request.ColorDTO;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
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
    public ResponseEntity<?> addColor(@RequestBody ColorCreateRequest colorDTO) {
        if (colorService.addColor(colorDTO)) {
            return ResponseEntity.status(200).body(new ApiResponse<>(200,null,"Color added"));
        }
        return ResponseEntity.status(400).body(new ErrorResponse(400,null, List.of("color failed to add")));
    }

    @GetMapping
    public ResponseEntity<?> getAllColors() {
        List<ColorDTO>colors= colorService.getAllColors();
        if(colors.isEmpty())
        {
            return ResponseEntity.status(204).body(new ApiResponse<>(204,null,"No color data"));
        }
        return ResponseEntity.status(200).body(new ApiResponse<>(200,colors,"Colors return" ));
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

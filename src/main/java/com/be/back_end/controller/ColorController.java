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
    public ResponseEntity<?> getColorById(@PathVariable String id) {
        try {
            Color color = colorService.getColorById(id);
            if (color != null) {
                return ResponseEntity.ok(new ApiResponse<>(200, color, "Color retrieved successfully"));
            }
            return ResponseEntity.status(404).body(new ErrorResponse(404, "Color not found", List.of("Invalid color ID: " + id)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ErrorResponse(500, "Unexpected error occurred", List.of(e.getMessage()))
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateColor(@PathVariable String id, @RequestBody ColorDTO colorDTO) {
        try {
            Color updatedColor = colorService.updateColor(id, colorDTO);
            if (updatedColor != null) {
                return ResponseEntity.ok(new ApiResponse<>(200, updatedColor, "Color updated successfully"));
            }
            return ResponseEntity.status(400).body(new ErrorResponse(400, "Color update failed", List.of("Invalid color ID: " + id)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ErrorResponse(500, "Unexpected error occurred", List.of(e.getMessage()))
            );
        }
    }


}

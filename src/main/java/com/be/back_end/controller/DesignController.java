package com.be.back_end.controller;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.DesignDTO;
import com.be.back_end.dto.request.CreateDesignRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.CreateDesignResponse;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.model.Designs;
import com.be.back_end.service.DesignService.IDesignService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

   /* @GetMapping
    public ResponseEntity<List<DesignDTO>> getAllDesigns() {
        List<DesignDTO> designs = designService.getAll();
        if (designs.isEmpty()) {
            System.out.println("No designs found.");
        }
        return ResponseEntity.ok(designs);
    }

    @PostMapping
    public ResponseEntity<DesignDTO> createDesign(@RequestBody DesignDTO designDTO) {
        DesignDTO createdDesign = designService.create(designDTO);
        System.out.println("Design created successfully.");
        return ResponseEntity.ok(createdDesign);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDesignById(@PathVariable String id) {
        DesignDTO design = designService.getById(id);
        if (design == null) {
            return ResponseEntity.badRequest().body("Design not found with ID: " + id);
        }
        return ResponseEntity.ok(design);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDesign(@PathVariable String id, @RequestBody DesignDTO designDTO) {
        boolean updated = designService.update(id, designDTO);
        if (!updated) {
            return ResponseEntity.badRequest().body("Failed to update. Account not found with ID: " + id);
        }
        return ResponseEntity.ok("Account updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDesign(@PathVariable String id) {
        boolean deleted = designService.removeById(id);
        if (!deleted) {
            return ResponseEntity.badRequest().body("Failed to delete. Design not found with ID: " + id);
        }
        return ResponseEntity.ok("Design deleted successfully.");
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createDesign(@ModelAttribute CreateDesignRequest createDesignRequest) {
        try {
            // Call service to process design creation
            CreateDesignResponse response = designService.createDesign(createDesignRequest);

            return ResponseEntity.ok(new ApiResponse<>(200, response, "Design created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(400, "Design creation failed", List.of(e.getMessage()))
            );
        }
    }*/
}

package com.be.back_end.controller;

import com.be.back_end.dto.request.ModificationRequest;
import com.be.back_end.dto.request.SetModificationRequest;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.service.ModificationRequestService.IModificationFormService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modification")
public class ModificationFormController {
    private final IModificationFormService modificationFormService;

    public ModificationFormController(IModificationFormService modificationFormService) {
        this.modificationFormService = modificationFormService;
    }


    @PutMapping("/update")
    public ResponseEntity<?> setModification(@Valid @RequestBody SetModificationRequest modificationRequest) {
        boolean isUpdated = modificationFormService.setModification(modificationRequest);
        if (isUpdated) {
            return ResponseEntity.ok(new ApiResponse<>(200, null, "Modification updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            400,
                            "Modification update failed",
                            List.of("Modification form or related data not found")
                    ));
        }
    }
    @PostMapping
    public ResponseEntity<?> addModificationForm(@Valid @RequestBody ModificationRequest modificationRequest) {
        try {
            boolean isAdded = modificationFormService.AddModificationForm(modificationRequest);

            if (isAdded) {
                return ResponseEntity.ok(new ApiResponse<>(200, null, "Modification form created successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(400, "Failed to create modification form",
                                List.of("Booking details not found or email delivery failed")));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "An error occurred", List.of(e.getMessage())));
        }
    }
}

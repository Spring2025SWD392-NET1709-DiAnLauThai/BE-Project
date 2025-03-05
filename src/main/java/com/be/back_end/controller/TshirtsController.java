package com.be.back_end.controller;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.TshirtsDTO;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.ErrorResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.model.Tshirts;
import com.be.back_end.service.AccountService.IAccountService;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import com.be.back_end.service.TshirtsService.ITshirtsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tshirts")
public class TshirtsController {
    private final ITshirtsService tshirtsService;
    private final ICloudinaryService cloudinaryService;

    public TshirtsController(ITshirtsService tshirtsService, ICloudinaryService cloudinaryService) {
        this.tshirtsService = tshirtsService;

        this.cloudinaryService = cloudinaryService;
    }



    @RequestMapping(
            path = "/tshirt/upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping("/tshirt/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = cloudinaryService.uploadFile(file);

        if (imageUrl == null) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Page and size must be positive values")));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, imageUrl, "Uploaded successfully"));
    }

    @GetMapping
    public ResponseEntity<?> getAllTshirt(@RequestParam(required = false) String keyword,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateFrom,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateTo,
                                          @RequestParam(defaultValue = "asc") String sortDir,
                                          @RequestParam(defaultValue = "createdAt") String sortBy) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Page and size must be positive values")));
        }
        PaginatedResponseDTO<TshirtsDTO> accounts = tshirtsService.getAllTshirts(
                keyword, page, size, dateFrom, dateTo, sortDir, sortBy
        );
        if (accounts.getContent().isEmpty()) {
            return ResponseEntity.status(204).body(new ApiResponse<>(204, null, "No data available"));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, accounts, "Page returned: " + page));
    }

}
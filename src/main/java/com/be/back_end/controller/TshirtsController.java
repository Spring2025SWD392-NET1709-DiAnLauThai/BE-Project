package com.be.back_end.controller;

import com.be.back_end.dto.response.*;
import com.be.back_end.dto.request.TshirtCreateRequest;
import com.be.back_end.model.Tshirts;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import com.be.back_end.service.TshirtsService.ITshirtsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tshirts")
public class TshirtsController {
    private final ITshirtsService tshirtsService;
    private final ICloudinaryService cloudinaryService;

    public TshirtsController(ITshirtsService tshirtsService, ICloudinaryService cloudinaryService) {
        this.tshirtsService = tshirtsService;

        this.cloudinaryService = cloudinaryService;
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateTshirt(@RequestBody TshirtsListDesignerResponse tshirtDto) {
        boolean isUpdated = tshirtsService.updateTshirt(tshirtDto);

        if (!isUpdated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(404, "Update Failed", List.of("T-shirt not found or invalid data.")));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, null, "T-shirt updated successfully"));
    }


    @PostMapping("/create")
    public ResponseEntity<?> tshirtupload(@RequestBody TshirtCreateRequest dto) {
        Tshirts tshirt = tshirtsService.saveTshirt(dto);
        if (tshirt == null) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Tshirt failed to add")));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Uploaded successfully"));
    }

    @PostMapping(
            value = "/upload/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, "File is empty", List.of("Please upload a valid file.")));
        }
        try {
            String imageUrl = cloudinaryService.uploadFile(file);
            if (imageUrl == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(500, "Upload failed", List.of("Failed to upload file to Cloudinary")));
            }
            return ResponseEntity.ok(new ApiResponse<>(200, imageUrl, "Uploaded successfully"));
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Server error", List.of("Unexpected error occurred.")));
        }
    }

    @PostMapping(
            value = "/upload/zip",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadZip(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(400, "File is empty", List.of("Please upload a valid file.")));
        }

        try {
            String imageUrl = cloudinaryService.uploadZipFile(file);
            if (imageUrl == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(500, "Upload failed", List.of("Failed to upload file to Cloudinary")));
            }
            return ResponseEntity.ok(new ApiResponse<>(200, imageUrl, "Uploaded successfully"));
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Server error", List.of("Unexpected error occurred.")));
        }
    }
    @GetMapping("/available")
    public ResponseEntity<?> getAllAvailableTshirt() {
        try {
            List<TshirtsListAvailableResponse> availableTshirts = tshirtsService.getAllTshirtsAvailable();

            if (availableTshirts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ApiResponse<>(204, null, "No available T-shirts found"));
            }

            return ResponseEntity.ok(new ApiResponse<>(200, availableTshirts, "Available T-shirts retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Server error", List.of("Unexpected error occurred.")));
        }
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
        PaginatedResponseDTO<TshirtsListDesignerResponse> accounts = tshirtsService.getAllTshirtsDesigner(
                keyword, page, size, dateFrom, dateTo, sortDir, sortBy
        );
        if (accounts.getContent().isEmpty()) {
            return ResponseEntity.status(204).body(new ApiResponse<>(204, null, "No data available"));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, accounts, "Page returned: " + page));
    }
    @GetMapping("/public")
    public ResponseEntity<?> getAllTshirtPublic(@RequestParam(required = false) String keyword,
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
        PaginatedResponseDTO<TshirtsListsResponse> accounts = tshirtsService.getAllTshirtCatalog(
                keyword, page, size, dateFrom, dateTo, sortDir, sortBy
        );
        if (accounts.getContent().isEmpty()) {
            return ResponseEntity.status(204).body(new ApiResponse<>(204, null, "No data available"));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, accounts, "Page returned: " + page));
    }
    @GetMapping("/tshirt/{tshirtId}")
    public ResponseEntity<?> getTshirtById(@PathVariable String tshirtId) {
        try {
            TshirtDetailResponse tshirtDetailResponse = tshirtsService.getTshirtById(tshirtId);
            if (tshirtDetailResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(404, "T-shirt not found", List.of("No T-shirt found with ID: " + tshirtId)));
            }
            return ResponseEntity.ok(new ApiResponse<>(200, tshirtDetailResponse, "T-shirt details retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(500, "Server error", List.of("Unexpected error occurred.")));
        }
    }


}
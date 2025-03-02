package com.be.back_end.controller;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.request.CreateAccountRequest;
import com.be.back_end.dto.request.UpdateAccountRequest;
import com.be.back_end.dto.request.UpdateProfileRequest;
import com.be.back_end.dto.response.*;
import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.service.AccountService.IAccountService;
import com.be.back_end.service.CloudinaryService.ICloudinaryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import com.be.back_end.dto.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestController
@RequestMapping("/api/accounts")
@SecurityRequirement(name = "Bearer Authentication")
public class AccountController {
    private final IAccountService accountService;
    private final ICloudinaryService cloudinaryService;

    public AccountController(IAccountService accountService, ICloudinaryService cloudinaryService) {
        this.accountService = accountService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        try {
            AccountCreationResponse response = accountService.createAccount(request);
            return ResponseEntity.ok(new ApiResponse<>(
                    201,
                    response,
                    "Account created successfully. Credentials sent to email."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of(e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(500, null, "Internal server error"));
        }
    }
    //tao 1 api upload file firebase su dung no cho update account, register

    @GetMapping
    public ResponseEntity<?> searchAccounts(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) RoleEnums role,

            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime dateTo,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Page and size must be positive values")));
        }
        PaginatedResponseDTO<AccountDTO> accounts = accountService.getAllUsers(
                keyword, page, size, role, dateFrom, dateTo, sortDir, sortBy
        );
        if (accounts.getContent().isEmpty()) {
            return ResponseEntity.status(204).body(new ApiResponse<>(204, null, "No data available"));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, accounts, "Page returned: " + page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable String id) {
        AccountDTO account = accountService.getUserById(id);
        if (account == null) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Account not found")));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, account, "Account returned"));
    }

    @PutMapping()
    public ResponseEntity<?> updateAccount(
            @RequestBody UpdateAccountRequest account
    ) {
        boolean updated = accountService.updateUser(account);
        if (!updated) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Account not found")));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Account updated"));
    }


    @PutMapping(path = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(@ModelAttribute UpdateProfileRequest account) {
        boolean updated = accountService.updateProfile(account, account.getImageFile());
        if (!updated) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Account failed to update")));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Account updated"));
    }




}

package com.be.back_end.controller;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.request.CreateAccountRequest;
import com.be.back_end.dto.response.AccountCreationResponse;
import com.be.back_end.dto.response.ApiResponse;
import com.be.back_end.dto.response.PaginatedResponseDTO;
import com.be.back_end.model.Account;
import com.be.back_end.service.AccountService.IAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import com.be.back_end.dto.response.ApiResponse;
@RestController
@RequestMapping("/api/accounts")
@SecurityRequirement(name = "Bearer Authentication")
public class AccountController {
    private final IAccountService accountService;
    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
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
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(400, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(500, null, "Internal server error"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts(@RequestParam int page, int size) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, null, "Page and size must be positive values"));
        }
        PaginatedResponseDTO<AccountDTO> accounts = accountService.getAllUsers(page, size);
        if (accounts.getContent().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ApiResponse<>(204, null, "No data available"));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, accounts, "Page returned: " + page));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable String id) {
        AccountDTO account = accountService.getUserById(id);
        if (account == null) {
            return ResponseEntity.badRequest().body("Account not found with ID: " + id);
        }
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@RequestBody AccountDTO account) {
        boolean updated = accountService.updateUser( account);
        if (!updated) {
            return ResponseEntity.badRequest().body("Failed to update. Account not found with ID: ");
        }
        return ResponseEntity.ok("Account updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable String id) {
        boolean deleted = accountService.deleteUser(id);
        if (!deleted) {
            return ResponseEntity.badRequest().body("Failed to delete. Account not found with ID: " + id);
        }
        return ResponseEntity.ok("Account deleted successfully.");
    }
}

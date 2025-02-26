package com.be.back_end.controller;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.request.AccountSearchCriteria;
import com.be.back_end.dto.request.CreateAccountRequest;
import com.be.back_end.dto.response.*;
import com.be.back_end.model.Account;
import com.be.back_end.service.AccountService.IAccountService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    //tao 1 api upload file firebase su dung no cho update account, register
    @GetMapping
    public ResponseEntity<?> searchAccounts(@RequestBody AccountSearchCriteria searchRequest) {
        if (searchRequest.getPage() < 0 || searchRequest.getSize() <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(400, null, "Page and size must be positive values"));
        }

        PaginatedResponseDTO<AccountDTO> accounts = accountService.getAllUsers(searchRequest);

        if (accounts.getContent().isEmpty()) {
            return ResponseEntity.status(204).body(new ApiResponse<>(204, null, "No data available"));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, accounts, "Page returned: " + searchRequest.getPage()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable String id) {
        AccountDTO account = accountService.getUserById(id);
        if (account == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, null, List.of("Account not found")));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, account, "Account returned"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@RequestBody AccountDTO account) {
        boolean updated = accountService.updateUser( account);
        if (!updated) {
            return ResponseEntity.badRequest().body("Failed to update. Account not found with ID: ");
        }
        return ResponseEntity.ok("Account updated successfully.");
    }


}

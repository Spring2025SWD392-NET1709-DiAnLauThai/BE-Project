package com.be.back_end.controller;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.dto.request.CreateAccountRequest;
import com.be.back_end.dto.response.*;
import com.be.back_end.enums.ActivationEnums;
import com.be.back_end.enums.RoleEnums;
import com.be.back_end.service.AccountService.IAccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import com.be.back_end.dto.response.ApiResponse;

import static org.springframework.web.servlet.function.ServerResponse.status;

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
            @RequestParam(required = false) ActivationEnums status,
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
                keyword, page, size, role,status, dateFrom, dateTo, sortDir, sortBy
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@RequestBody AccountDTO account) {
        boolean updated = accountService.updateUser( account);
        if (!updated) {
            return ResponseEntity.status(400)
                    .body(new ErrorResponse(400, null, List.of("Account not found")));
        }
        return ResponseEntity.ok(new ApiResponse<>(200, null, "Account updated"));
    }


}

package com.be.back_end.controller;

import com.be.back_end.dto.AccountDTO;
import com.be.back_end.model.Account;
import com.be.back_end.service.AccountService.IAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final IAccountService accountService;
    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO account) {
        AccountDTO createdAccount = accountService.createUser(account);
        System.out.println("Account created successfully.");
        return ResponseEntity.ok(createdAccount);
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllUsers();
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        }
        return ResponseEntity.ok(accounts);
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
    public ResponseEntity<?> updateAccount(@PathVariable String id, @RequestBody AccountDTO account) {
        boolean updated = accountService.updateUser(id, account);
        if (!updated) {
            return ResponseEntity.badRequest().body("Failed to update. Account not found with ID: " + id);
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

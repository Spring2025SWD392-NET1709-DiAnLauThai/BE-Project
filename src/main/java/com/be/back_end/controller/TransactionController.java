package com.be.back_end.controller;

import com.be.back_end.dto.TransactionDTO;

import com.be.back_end.service.TransactionService.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;



@RestController
@RequestMapping("/api/payments")
public class TransactionController {

    private final ITransactionService transactionService;


    @Autowired
    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;

    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTranscations() {
        List<TransactionDTO> payments = transactionService.getAll();
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
        }
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTranscation(@RequestBody TransactionDTO TransactionDTO) {
        TransactionDTO createdDesign = transactionService.create(TransactionDTO);
        System.out.println("Payment created successfully.");
        return ResponseEntity.ok(createdDesign);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTranscationById(@PathVariable String id) {
        TransactionDTO design = transactionService.getById(id);
        if (design == null) {
            return ResponseEntity.badRequest().body("Payment not found with ID: " + id);
        }
        return ResponseEntity.ok(design);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTranscation(@PathVariable String id, @RequestBody TransactionDTO TransactionDTO) {
        boolean updated = transactionService.update(id, TransactionDTO);
        if (!updated) {
            return ResponseEntity.badRequest().body("Failed to update. Payment not found with ID: " + id);
        }
        return ResponseEntity.ok("Payment updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTranscation(@PathVariable String id) {
        boolean deleted = transactionService.delete(id);
        if (!deleted) {
            return ResponseEntity.badRequest().body("Failed to delete. Payment not found with ID: " + id);
        }
        return ResponseEntity.ok("Payment deleted successfully.");
    }


}

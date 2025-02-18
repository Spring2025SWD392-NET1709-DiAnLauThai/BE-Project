package com.be.back_end.controller;

import com.be.back_end.dto.PaymentDTO;

import com.be.back_end.service.PaymentService.IPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final IPaymentService paymentService;

    public PaymentController(IPaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayment() {
        List<PaymentDTO> payments = paymentService.getAll();
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
        }
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody PaymentDTO PaymentDTO) {
        PaymentDTO createdDesign = paymentService.create(PaymentDTO);
        System.out.println("Payment created successfully.");
        return ResponseEntity.ok(createdDesign);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable String id) {
        PaymentDTO design = paymentService.getById(id);
        if (design == null) {
            return ResponseEntity.badRequest().body("Payment not found with ID: " + id);
        }
        return ResponseEntity.ok(design);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@PathVariable String id, @RequestBody PaymentDTO PaymentDTO) {
        boolean updated = paymentService.update(id, PaymentDTO);
        if (!updated) {
            return ResponseEntity.badRequest().body("Failed to update. Payment not found with ID: " + id);
        }
        return ResponseEntity.ok("Payment updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable String id) {
        boolean deleted = paymentService.delete(id);
        if (!deleted) {
            return ResponseEntity.badRequest().body("Failed to delete. Payment not found with ID: " + id);
        }
        return ResponseEntity.ok("Payment deleted successfully.");
    }
}

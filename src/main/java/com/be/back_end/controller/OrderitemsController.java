package com.be.back_end.controller;

import com.be.back_end.dto.OrderitemsDTO;
import com.be.back_end.model.Orderitems;
import com.be.back_end.service.OrderItemService.IOrderitemsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderitems")
public class OrderitemsController {
    private final IOrderitemsService orderitemsService;

    public OrderitemsController(IOrderitemsService orderitemsService) {
        this.orderitemsService = orderitemsService;
    }

    @PostMapping
    public ResponseEntity<String> addOrderItem(@RequestBody OrderitemsDTO dto) {
        if (orderitemsService.createOrderItem(dto)) {
            return ResponseEntity.ok("Order item added");
        }
        return ResponseEntity.badRequest().body("Failed to add order item");
    }

    @GetMapping
    public ResponseEntity<List<Orderitems>> getAllOrderItems() {
        return ResponseEntity.ok(orderitemsService.getAllOrderItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orderitems> getOrderItemById(@PathVariable String id) {
        Orderitems orderItem = orderitemsService.getOrderItemById(id);
        return orderItem != null ? ResponseEntity.ok(orderItem) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orderitems> updateOrderItem(@PathVariable String id, @RequestBody OrderitemsDTO dto) {
        Orderitems updatedOrderItem = orderitemsService.updateOrderItem(id, dto);
        return updatedOrderItem != null ? ResponseEntity.ok(updatedOrderItem) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderItem(@PathVariable String id) {
        return orderitemsService.deleteOrderItem(id) ? ResponseEntity.ok("Order item deleted") : ResponseEntity.badRequest().body("Order item not found");
    }
}
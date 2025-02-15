package com.be.back_end.controller;

import com.be.back_end.dto.OrderDTO;
import com.be.back_end.model.Orders;
import com.be.back_end.service.OrderService.IOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> addOrder(@RequestBody OrderDTO orderDTO) {
        if (orderService.createOrder(orderDTO)) {
            return ResponseEntity.ok("Order added");
        }
        return ResponseEntity.badRequest().body("Order failed to add");
    }

    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable String id) {
        Orders order = orderService.getOrderById(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orders> updateOrder(@PathVariable String id, @RequestBody OrderDTO orderDTO) {
        Orders updatedOrder = orderService.updateOrder(id, orderDTO);
        return updatedOrder != null ? ResponseEntity.ok(updatedOrder) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable String id) {
        return orderService.deleteOrder(id) ? ResponseEntity.ok("Order deleted") : ResponseEntity.badRequest().body("Order not found");
    }
}
package com.jsp.bookstore.controller;

import com.jsp.bookstore.entity.Order;
import com.jsp.bookstore.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping("/place/{userId}")
    public ResponseEntity<Order> place(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(userId));
    }

    @GetMapping
    public List<Order> all() { return orderService.getAllOrders(); }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) { return orderService.getOrderById(id); }

    @GetMapping("/user/{userId}")
    public List<Order> byUser(@PathVariable Long userId) { return orderService.getOrdersByUser(userId); }

    @PutMapping("/{id}/status")
    public Order status(@PathVariable Long id, @RequestParam String status) {
        return orderService.updateStatus(id, status);
    }

    @PutMapping("/{id}/cancel")
    public Order cancel(@PathVariable Long id) { return orderService.cancelOrder(id); }
}

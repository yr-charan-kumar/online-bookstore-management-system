package com.jsp.bookstore.controller;

import com.jsp.bookstore.entity.Cart;
import com.jsp.bookstore.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) { this.cartService = cartService; }

    @GetMapping("/{userId}")
    public Cart get(@PathVariable Long userId) { return cartService.getCart(userId); }

    @PostMapping("/{userId}/add")
    public Cart add(@PathVariable Long userId, @RequestParam Long bookId, @RequestParam(defaultValue = "1") int quantity) {
        return cartService.addToCart(userId, bookId, quantity);
    }

    @PutMapping("/{userId}/update")
    public Cart update(@PathVariable Long userId, @RequestParam Long bookId, @RequestParam int quantity) {
        return cartService.updateQuantity(userId, bookId, quantity);
    }

    @DeleteMapping("/{userId}/remove/{bookId}")
    public Cart remove(@PathVariable Long userId, @PathVariable Long bookId) {
        return cartService.removeFromCart(userId, bookId);
    }

    @DeleteMapping("/{userId}/clear")
    public Map<String, String> clear(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return Map.of("message", "Cart cleared");
    }
}

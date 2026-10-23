package com.jsp.bookstore.service;

import com.jsp.bookstore.entity.Cart;

public interface CartService {
    Cart getCart(Long userId);
    Cart addToCart(Long userId, Long bookId, int quantity);
    Cart updateQuantity(Long userId, Long bookId, int quantity);
    Cart removeFromCart(Long userId, Long bookId);
    void clearCart(Long userId);
}

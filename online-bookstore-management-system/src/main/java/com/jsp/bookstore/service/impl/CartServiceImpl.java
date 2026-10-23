package com.jsp.bookstore.service.impl;

import com.jsp.bookstore.entity.*;
import com.jsp.bookstore.exception.BookNotFoundException;
import com.jsp.bookstore.exception.InsufficientStockException;
import com.jsp.bookstore.exception.UserNotFoundException;
import com.jsp.bookstore.repository.*;
import com.jsp.bookstore.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Cart getCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    @Override
    public Cart addToCart(Long userId, Long bookId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0");
        Cart cart = getCart(userId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + bookId));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(bookId)).findFirst().orElse(null);
        int newQty = (item == null ? 0 : item.getQuantity()) + quantity;
        if (newQty > book.getStock()) {
            throw new InsufficientStockException("Only " + book.getStock() + " copies of '" + book.getTitle() + "' in stock");
        }
        if (item == null) {
            item = new CartItem();
            item.setCart(cart);
            item.setBook(book);
            cart.getItems().add(item);
        }
        item.setQuantity(newQty);
        return cartRepository.save(cart);
    }

    @Override
    public Cart updateQuantity(Long userId, Long bookId, int quantity) {
        Cart cart = getCart(userId);
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(bookId)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("Book " + bookId + " is not in the cart"));
        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            if (quantity > item.getBook().getStock()) {
                throw new InsufficientStockException("Only " + item.getBook().getStock() + " copies in stock");
            }
            item.setQuantity(quantity);
        }
        return cartRepository.save(cart);
    }

    @Override
    public Cart removeFromCart(Long userId, Long bookId) {
        Cart cart = getCart(userId);
        boolean removed = cart.getItems().removeIf(i -> i.getBook().getId().equals(bookId));
        if (!removed) throw new NoSuchElementException("Book " + bookId + " is not in the cart");
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}

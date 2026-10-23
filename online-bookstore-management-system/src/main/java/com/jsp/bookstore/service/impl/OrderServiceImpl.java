package com.jsp.bookstore.service.impl;

import com.jsp.bookstore.entity.*;
import com.jsp.bookstore.exception.InsufficientStockException;
import com.jsp.bookstore.repository.BookRepository;
import com.jsp.bookstore.repository.OrderRepository;
import com.jsp.bookstore.service.CartService;
import com.jsp.bookstore.service.OrderService;
import com.jsp.bookstore.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Set<String> STATUSES = Set.of("PLACED", "SHIPPED", "DELIVERED", "CANCELLED");

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final CartService cartService;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, BookRepository bookRepository,
                            CartService cartService, UserService userService) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.cartService = cartService;
        this.userService = userService;
    }

    @Override
    public Order placeOrder(Long userId) {
        User user = userService.getUserById(userId);
        Cart cart = cartService.getCart(userId);
        if (cart.getItems().isEmpty()) throw new IllegalStateException("Cart is empty");

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cart.getItems()) {
            Book book = ci.getBook();
            if (ci.getQuantity() > book.getStock()) {
                throw new InsufficientStockException("Insufficient stock for '" + book.getTitle()
                        + "'. Available: " + book.getStock());
            }
            book.setStock(book.getStock() - ci.getQuantity());
            bookRepository.save(book);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setBook(book);
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(book.getPrice());
            order.getItems().add(oi);
            total = total.add(book.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }
        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);
        cartService.clearCart(userId);
        return saved;
    }

    @Override @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id " + id));
    }

    @Override @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(Long userId) {
        userService.getUserById(userId);
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    @Override @Transactional(readOnly = true)
    public List<Order> getAllOrders() { return orderRepository.findAll(); }

    @Override
    public Order updateStatus(Long id, String status) {
        String s = status == null ? "" : status.toUpperCase();
        if (!STATUSES.contains(s)) throw new IllegalArgumentException("Status must be one of " + STATUSES);
        if (s.equals("CANCELLED")) return cancelOrder(id);
        Order order = getOrderById(id);
        if (order.getStatus().equals("CANCELLED")) throw new IllegalStateException("Order is cancelled");
        order.setStatus(s);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long id) {
        Order order = getOrderById(id);
        if (order.getStatus().equals("CANCELLED")) throw new IllegalStateException("Order already cancelled");
        if (order.getStatus().equals("DELIVERED")) throw new IllegalStateException("Delivered orders cannot be cancelled");
        for (OrderItem oi : order.getItems()) {
            Book b = oi.getBook();
            b.setStock(b.getStock() + oi.getQuantity());
            bookRepository.save(b);
        }
        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }
}

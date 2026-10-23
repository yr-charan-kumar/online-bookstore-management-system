package com.jsp.bookstore.service;

import com.jsp.bookstore.entity.Order;
import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);
    Order getOrderById(Long id);
    List<Order> getOrdersByUser(Long userId);
    List<Order> getAllOrders();
    Order updateStatus(Long id, String status);
    Order cancelOrder(Long id);
}

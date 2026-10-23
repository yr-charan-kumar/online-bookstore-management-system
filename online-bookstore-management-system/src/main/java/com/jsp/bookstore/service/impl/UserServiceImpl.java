package com.jsp.bookstore.service.impl;

import com.jsp.bookstore.entity.User;
import com.jsp.bookstore.exception.UserNotFoundException;
import com.jsp.bookstore.repository.CartRepository;
import com.jsp.bookstore.repository.UserRepository;
import com.jsp.bookstore.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public UserServiceImpl(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + user.getEmail());
        }
        user.setId(null);
        if (user.getRole() == null || user.getRole().isBlank()) user.setRole("CUSTOMER");
        return userRepository.save(user);
    }

    @Override @Transactional(readOnly = true)
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("No user with email " + email));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return user;
    }

    @Override @Transactional(readOnly = true)
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @Override @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    @Override
    public User updateUser(Long id, User u) {
        User existing = getUserById(id);
        if (u.getName() != null) existing.setName(u.getName());
        existing.setPhone(u.getPhone());
        existing.setAddress(u.getAddress());
        if (u.getPassword() != null && !u.getPassword().isBlank()) existing.setPassword(u.getPassword());
        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        cartRepository.findByUserId(id).ifPresent(cartRepository::delete);
        userRepository.delete(user);
    }
}

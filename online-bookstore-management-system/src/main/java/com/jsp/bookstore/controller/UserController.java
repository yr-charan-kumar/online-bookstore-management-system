package com.jsp.bookstore.controller;

import com.jsp.bookstore.entity.User;
import com.jsp.bookstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(user));
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> creds) {
        return userService.login(creds.get("email"), creds.get("password"));
    }

    @GetMapping
    public List<User> all() { return userService.getAllUsers(); }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) { return userService.getUserById(id); }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) { return userService.updateUser(id, user); }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return Map.of("message", "User deleted successfully");
    }
}

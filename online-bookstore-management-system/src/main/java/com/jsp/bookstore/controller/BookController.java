package com.jsp.bookstore.controller;

import com.jsp.bookstore.entity.Book;
import com.jsp.bookstore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) { this.bookService = bookService; }

    @PostMapping
    public ResponseEntity<Book> add(@Valid @RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(book));
    }

    @GetMapping
    public List<Book> all() { return bookService.getAllBooks(); }

    @GetMapping("/{id}")
    public Book get(@PathVariable Long id) { return bookService.getBookById(id); }

    @PutMapping("/{id}")
    public Book update(@PathVariable Long id, @Valid @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return Map.of("message", "Book deleted successfully");
    }

    @GetMapping("/search/title")
    public List<Book> byTitle(@RequestParam String title) { return bookService.searchByTitle(title); }

    @GetMapping("/search/author")
    public List<Book> byAuthor(@RequestParam String author) { return bookService.searchByAuthor(author); }

    @GetMapping("/category/{category}")
    public List<Book> byCategory(@PathVariable String category) { return bookService.getByCategory(category); }
}

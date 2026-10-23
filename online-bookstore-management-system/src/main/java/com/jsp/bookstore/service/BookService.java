package com.jsp.bookstore.service;

import com.jsp.bookstore.entity.Book;
import java.util.List;

public interface BookService {
    Book addBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthor(String author);
    List<Book> getByCategory(String category);
}

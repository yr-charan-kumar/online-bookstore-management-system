package com.jsp.bookstore.service.impl;

import com.jsp.bookstore.entity.Book;
import com.jsp.bookstore.exception.BookNotFoundException;
import com.jsp.bookstore.repository.BookRepository;
import com.jsp.bookstore.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBook(Book book) {
        if (book.getIsbn() != null && bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        book.setId(null);
        return bookRepository.save(book);
    }

    @Override @Transactional(readOnly = true)
    public List<Book> getAllBooks() { return bookRepository.findAll(); }

    @Override @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id " + id));
    }

    @Override
    public Book updateBook(Long id, Book b) {
        Book existing = getBookById(id);
        existing.setTitle(b.getTitle());
        existing.setAuthor(b.getAuthor());
        existing.setIsbn(b.getIsbn());
        existing.setCategory(b.getCategory());
        existing.setPrice(b.getPrice());
        existing.setStock(b.getStock());
        return bookRepository.save(existing);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.delete(getBookById(id));
    }

    @Override @Transactional(readOnly = true)
    public List<Book> searchByTitle(String title) { return bookRepository.findByTitleContainingIgnoreCase(title); }

    @Override @Transactional(readOnly = true)
    public List<Book> searchByAuthor(String author) { return bookRepository.findByAuthorContainingIgnoreCase(author); }

    @Override @Transactional(readOnly = true)
    public List<Book> getByCategory(String category) { return bookRepository.findByCategoryIgnoreCase(category); }
}

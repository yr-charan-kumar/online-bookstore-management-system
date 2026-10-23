package com.jsp.bookstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "books")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @Column(unique = true)
    private String isbn;

    private String category;

    @NotNull @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;
}

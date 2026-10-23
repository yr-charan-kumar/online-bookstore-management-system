package com.jsp.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank @Email(message = "Valid email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank @Size(min = 4, message = "Password must be at least 4 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String phone;
    private String address;

    @Column(nullable = false)
    private String role = "CUSTOMER";
}

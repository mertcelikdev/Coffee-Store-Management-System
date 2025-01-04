package com.ozcelikkahve.ozcelikcoffee.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="users")

public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;  // User ID

    @Column(name = "email")
    private String email;  // User's email

    @Column(name = "password")
    private String password;  // User's password

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;  // User's role (Many users can have the same role)

    // Diğer ilişkiler ve metotlar
}

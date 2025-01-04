package com.ozcelikkahve.ozcelikcoffee.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="employees")

public class Employees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;  // Employee ID

    @Column(name = "first_name", nullable = false)
    private String firstName;  // First name

    @Column(name = "last_name", nullable = false)
    private String lastName;  // Last name

    @Column(name = "email", nullable = false, unique = true)
    private String email;  // Email

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Roles role;  // Employee's role (Many employees can have the same role)

    @Column(name = "salary", nullable = false)
    private Double salary;  // Salary

    @Column(name="image_url")
    private String imageUrl;
}

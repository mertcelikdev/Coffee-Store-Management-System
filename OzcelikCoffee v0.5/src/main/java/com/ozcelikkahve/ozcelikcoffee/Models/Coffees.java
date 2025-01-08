package com.ozcelikkahve.ozcelikcoffee.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="coffees")
public class Coffees {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coffee_id")
    private Long coffee_id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "size")
    private String size;
    @Column(name = "price")
    private double price;
    @Column(name = "stock")
    private Integer stock;
    @Column(name = "images")
    private String images;


}

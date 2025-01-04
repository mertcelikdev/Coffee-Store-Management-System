package com.ozcelikkahve.ozcelikcoffee.Models;

import jakarta.persistence.*;
import lombok.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="desserts")

public class Desserts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="dessert_id")
    private Long dessert_id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "price")
    private Integer price;
    @Column(name="stock")
    private Integer stock;
    @Column(name = "images")
    private String images;



}

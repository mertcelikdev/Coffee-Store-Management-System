package com.ozcelikkahve.ozcelikcoffee.Controllers;


import com.ozcelikkahve.ozcelikcoffee.Models.Coffees;
import com.ozcelikkahve.ozcelikcoffee.Models.Employees;
import com.ozcelikkahve.ozcelikcoffee.Repositories.CoffeeRepository;
import com.ozcelikkahve.ozcelikcoffee.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/coffees")
public class CoffeeController {

    private final CoffeeRepository coffeeRepository;

    @Autowired
    CoffeeController(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    @GetMapping
    public List<Coffees> getAllEmployees() {
        return coffeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Coffees> getCoffeeById(@PathVariable Long id) {
        return coffeeRepository.findById(id);
    }


    @PostMapping
    public Coffees createCoffee(@RequestBody Coffees coffees) {
        return coffeeRepository.save(coffees);
    }

    @PutMapping("/{id}")
    public Coffees updateCoffees(@PathVariable Long id, @RequestBody Coffees updatedCoffees) {
        return coffeeRepository.findById(id)  // ID Long türünde olmalı
                .map(existingCoffee -> {
                    // Sadece güncellenen alanlar set ediliyor
                    existingCoffee.setName(updatedCoffees.getName());
                    existingCoffee.setSize(updatedCoffees.getSize());
                    existingCoffee.setPrice(updatedCoffees.getPrice());
                    existingCoffee.setDescription(updatedCoffees.getDescription());  // toString yerine setDescription
                    existingCoffee.setAvailable(updatedCoffees.isAvailable());

                    // Güncellenmiş kahve nesnesi geri kaydediliyor
                    return coffeeRepository.save(existingCoffee);
                }).orElseThrow(() -> new RuntimeException("Coffee not found"));
    }


    // Bir çalışanı sil
    @DeleteMapping("/{id}")
    public void deleteCoffeeById(@PathVariable Long id) {
        coffeeRepository.deleteById(id);
    }
}




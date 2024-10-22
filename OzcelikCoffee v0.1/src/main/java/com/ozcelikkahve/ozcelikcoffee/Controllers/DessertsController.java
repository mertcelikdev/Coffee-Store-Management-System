package com.ozcelikkahve.ozcelikcoffee.Controllers;

import com.ozcelikkahve.ozcelikcoffee.Models.Desserts;
import com.ozcelikkahve.ozcelikcoffee.Models.Employees;
import com.ozcelikkahve.ozcelikcoffee.Repositories.DessertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/desserts")
public class DessertsController {

    private DessertRepository dessertRepository;

    //Constructor injection kullanımı
    @Autowired
    public DessertsController(DessertRepository dessertRepository) {
        this.dessertRepository = dessertRepository;
    }

    // Tüm tatlıları getir
    @GetMapping
    public List<Desserts> getAllDesserts() {
        return dessertRepository.findAll();
    }

    // Belirli bir tatlıyı getir
    @GetMapping("/{id}")
    public Optional<Desserts> getDessertsById(@PathVariable Long id) {
        return dessertRepository.findById((id));
    }

    // Yeni bir çalışan ekle
    @PostMapping
    public Desserts createDesserts(@RequestBody Desserts desserts) {
        return dessertRepository.save(desserts);
    }

    // Var olan bir tatlıyı güncelle
    @PutMapping("/{id}")
    public Desserts updateDesserts(@PathVariable Long id, @RequestBody Desserts desserts) {
        return dessertRepository.findById((id))
                .map(desserts1 -> {
                    desserts1.setName(desserts.getName());
                    desserts1.setAvailable(desserts.getAvailable());
                    desserts1.setPrice(desserts.getPrice());
                    desserts1.setDescription(desserts.getDescription());

                    return dessertRepository.save(desserts);
                }).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // Bir tatlıyı sil
    @DeleteMapping("/{id}")
    public void deleteDessert(@PathVariable Long id) {
        dessertRepository.deleteById((id));
    }
}



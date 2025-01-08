package com.ozcelikkahve.ozcelikcoffee.Controllers;

import com.ozcelikkahve.ozcelikcoffee.Models.Coffees;
import com.ozcelikkahve.ozcelikcoffee.Repositories.CoffeeRepository;
import com.ozcelikkahve.ozcelikcoffee.Services.FilesStorageServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/coffees")
public class CoffeeController {

    @Autowired
    private final CoffeeRepository coffeeRepository;

    @Setter
    @Getter
    @Autowired
    private FilesStorageServiceImpl filesStorageServiceImpl;

    public CoffeeController(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    // Tüm kahveleri listele
    @GetMapping
    public String getCoffees(Model model) {
        Iterable<Coffees> coffees = coffeeRepository.findAll();
        model.addAttribute("coffees", coffees); // Tüm kahve listesini modele ekle
        return "Coffees/Coffees"; // Kahve listesini göstermek için şablon dosyası (Coffees.html)
    }

    // Yeni kahve ekleme formunu göster
    @GetMapping("/add")
    public String addCoffeeForm(Model model) {
        model.addAttribute("coffees", new Coffees()); // Yeni kahve için boş bir nesne gönder
        return "Coffees/AddCoffee"; // Yeni kahve ekleme sayfası (AddCoffee.html)
    }

    // Yeni kahve ekleme işlemi
    @PostMapping("/add")
    public String addCoffee(@RequestParam("name") String name,
                            @RequestParam("type") String type,
                            @RequestParam("size") String size,
                            @RequestParam("price") double price,
                            @RequestParam("stock") int stock,
                            @RequestParam("file") MultipartFile file) {

        // Dosyayı "static/resources/images" dizinine kaydedin
        String uploadDir = "src/main/resources/static/images/";

        Path path = Paths.get(uploadDir + file.getOriginalFilename());

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while uploading file");
        }

        // Veritabanında saklanacak URL
        String imageUrl = "/images/" + file.getOriginalFilename();  // Görsel URL'sini düzenledik

        // Kahve objesi oluştur
        Coffees coffee = new Coffees();
        coffee.setName(name);
        coffee.setType(type);
        coffee.setSize(size);
        coffee.setPrice(price);
        coffee.setStock(stock);
        coffee.setImages(imageUrl);  // Veritabanına fotoğraf yolunu ekliyoruz

        // Kahveyi veritabanına kaydet
        coffeeRepository.save(coffee);

        return "redirect:/coffees";  // Başka bir sayfaya yönlendir
    }

    // Kahve güncelleme formunu göster
    @GetMapping("/update/{coffee_id}")
    public String updateCoffeeForm(@PathVariable("coffee_id") Long id, Model model) {
        Coffees coffee = coffeeRepository.findById(id).orElse(null); // ID ile kahveyi bul
        model.addAttribute("coffees", coffee); // Güncelleme formuna kahveyi gönder
        return "Coffees/UpdateCoffee"; // Güncelleme formu şablonu (UpdateCoffee.html)
    }

    // Kahve güncelleme işlemi
    @PostMapping("/update/{id}")
    public String updateCoffee(
            @PathVariable Long id,
            @ModelAttribute("coffee") Coffees coffee,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            // Mevcut kahveyi veritabanından al
            Coffees existingCoffee = coffeeRepository.findById(id).orElse(null);

            if (existingCoffee == null) {
                throw new RuntimeException("Coffee not found");
            }

            // Yeni görsel yüklenmişse, eski görseli yer değiştir
            if (file != null && !file.isEmpty()) {
                // Eğer mevcut bir görsel varsa, onun adını al
                String oldImageName = existingCoffee.getImages();

                // Yeni görseli kaydet
                String newImagePath = "src/main/resources/static/images/coffees/" + file.getOriginalFilename();
                Path targetLocation = Paths.get(newImagePath);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                // Eski bir görsel varsa, eski görseli sil
                if (oldImageName != null && !oldImageName.isEmpty()) {
                    String oldImagePath = "src/main/resources/static/images/coffees/" + oldImageName;
                    Path oldImageFile = Paths.get(oldImagePath);

                    // Eski görseli sil
                    Files.deleteIfExists(oldImageFile);
                }

                // Yeni görselin yolunu veritabanına kaydet
                existingCoffee.setImages(file.getOriginalFilename()); // Görsel yolunu düzenliyoruz
            } else {
                // Yeni görsel yüklenmediyse, eski görselin yolunu olduğu gibi bırak
                existingCoffee.setImages(existingCoffee.getImages());
            }

            // Kahveyi güncelle
            existingCoffee.setName(coffee.getName());
            existingCoffee.setType(coffee.getType());
            existingCoffee.setSize(coffee.getSize());
            existingCoffee.setPrice(coffee.getPrice());
            existingCoffee.setStock(coffee.getStock());

            // Kahveyi veritabanına kaydet
            coffeeRepository.save(existingCoffee);

            return "redirect:/coffees"; // Güncellendikten sonra listeye yönlendir

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while updating coffee image");
        }
    }


    // Kahve silme işlemi
    @PostMapping("/delete/{coffee_id}")
    public String deleteCoffee(@PathVariable("coffee_id") Long id) {
        coffeeRepository.deleteById(id); // ID ile kahveyi sil
        return "redirect:/coffees"; // Silindikten sonra kahve listesine yönlendir
    }
}

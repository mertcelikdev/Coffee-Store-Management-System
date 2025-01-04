package com.ozcelikkahve.ozcelikcoffee.Controllers;

import com.ozcelikkahve.ozcelikcoffee.Models.Coffees;
import com.ozcelikkahve.ozcelikcoffee.Models.Desserts;
import com.ozcelikkahve.ozcelikcoffee.Repositories.DessertRepository;
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
import java.util.List;

@Controller
@RequestMapping("/desserts")
public class DessertsController {

    private final DessertRepository dessertRepository;

    // Constructor injection kullanımı
    @Autowired
    public DessertsController(DessertRepository dessertRepository) {
        this.dessertRepository = dessertRepository;
    }

    // Tüm tatlıları getir ve Thymeleaf'e gönder
    @GetMapping
    public String getAllDesserts(Model model) {
        Iterable<Desserts> desserts = dessertRepository.findAll();
        model.addAttribute("desserts", desserts);
        return "Desserts/Desserts"; // Desserts listesi için Thymeleaf view
    }

    // ID ile tatlıyı getir

    @GetMapping("/add")
    public String addDessert(Model model){
        Desserts desserts = new Desserts();
        model.addAttribute("desserts", desserts);
        return "Desserts/AddDessert";
    }

    @PostMapping("/add")
    public String addDessert(@RequestParam("name") String name,
                             @RequestParam("price") Integer price,
                             @RequestParam("type") String type,
                             @RequestParam("stock") int stock,
                             @RequestParam("file") MultipartFile file) {

        // Dosyayı "static/resources/images/desserts" dizinine kaydedin
        String uploadDir = "src/main/resources/static/images/desserts/";
        Path path = Paths.get(uploadDir + "/" + file.getOriginalFilename());

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while uploading file");
        }

        // Veritabanında saklanacak URL
        String imageUrl = file.getOriginalFilename();

        // Tatlı objesi oluştur
        Desserts dessert = new Desserts();
        dessert.setName(name);
        dessert.setPrice(price);
        dessert.setType(type);
        dessert.setStock(stock);
        dessert.setImages(imageUrl);  // Veritabanına fotoğraf yolunu ekliyoruz

        // Tatlıyı veritabanına kaydet
        dessertRepository.save(dessert);

        return "redirect:/desserts";  // Başka bir sayfaya yönlendir
    }


    //


    // Kahve güncelleme formunu göster
    @GetMapping("/update/{id}")
    public String updateDessertById(@PathVariable Long id, Model model) {
        Desserts desserts = dessertRepository.findById(id).orElse(null);
        if (desserts == null) {
            return "redirect:/coffees"; // Eğer kahve bulunamazsa listeye dön
        }
        model.addAttribute("desserts", desserts);
        return "Desserts/UpdateDessert"; // Güncelleme formu
    }

    @PostMapping("/update/{id}")
    public String updateDessert(@PathVariable Long id,
                                @ModelAttribute("desserts") Desserts desserts,
                                @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            // Mevcut tatlıyı veritabanından al
            Desserts existingDessert = dessertRepository.findById(id).orElse(null);

            if (existingDessert == null) {
                throw new RuntimeException("Dessert not found");
            }

            // Yeni görsel yüklenmişse, eski görseli yer değiştir
            if (file != null && !file.isEmpty()) {
                // Eğer mevcut bir görsel varsa, onun adını al
                String oldImageName = existingDessert.getImages();

                // Yeni görseli kaydet
                String newImagePath = "src/main/resources/static/images/desserts/" + file.getOriginalFilename();
                Path targetLocation = Paths.get(newImagePath);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                // Eğer eski bir görsel varsa, eski görsel ile yeni görselin adlarını değiştir
                if (oldImageName != null && !oldImageName.isEmpty()) {
                    String oldImagePath = "src/main/resources/static/images/desserts/" + oldImageName;
                    Path oldImageFile = Paths.get(oldImagePath);

                    // Eski görselin adını yeni görselin adıyla değiştir
                    Path newOldImageFilePath = Paths.get("src/main/resources/static/images/desserts/" + file.getOriginalFilename());
                    Files.move(oldImageFile, newOldImageFilePath, StandardCopyOption.REPLACE_EXISTING);
                }

                // Yeni görselin yolunu veritabanına kaydet
                existingDessert.setImages(file.getOriginalFilename()); // Görsel yolunu düzenliyoruz
            } else {
                // Eğer yeni görsel yüklenmediyse, eski görselin yolunu olduğu gibi bırak
                existingDessert.setImages(existingDessert.getImages());
            }

            // Tatlının diğer özelliklerini güncelle
            existingDessert.setName(desserts.getName());
            existingDessert.setType(desserts.getType());
            existingDessert.setPrice(desserts.getPrice());
            existingDessert.setStock(desserts.getStock());

            // Güncellenen tatlıyı veritabanına kaydet
            dessertRepository.save(existingDessert);

            return "redirect:/desserts"; // Güncellenen tatlıyı listeye yönlendir
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while updating dessert image");
        }
    }





    // Kahve silme işlemi
    @PostMapping("/delete/{id}")
    public String deleteDessert(@PathVariable Long id) {
        dessertRepository.deleteById(id);
        return "redirect:/desserts"; // Silindikten sonra listeye yönlendir
    }
}

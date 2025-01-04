package com.ozcelikkahve.ozcelikcoffee.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Ana sayfa yönlendirmesi
    @GetMapping("/")
    public String home() {
        return "Home/Home"; // home.html dosyasına yönlendirir
    }

    // Hakkımızda sayfası için yönlendirme
    @GetMapping("/about")
    public String about() {
        return "Home/About"; // about.html dosyasına yönlendirir (Eğer bu sayfa varsa)
    }

    // İletişim sayfası için yönlendirme
    @GetMapping("/contact")
    public String contact() {
        return "Home/Contact"; // contact.html dosyasına yönlendirir (Eğer bu sayfa varsa)
    }
}

package com.ozcelikkahve.ozcelikcoffee.Controllers;

import com.ozcelikkahve.ozcelikcoffee.Models.Users;
import com.ozcelikkahve.ozcelikcoffee.Repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class AuthenticationController {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String showMyLoginPage() {
        return "Home/Login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, Model model) {
        Users user = userRepository.findByEmail(email);

        // Şifreyi kontrol et
        if (user != null && password.equals(user.getPassword())) {
            // Başarılı giriş
            return "redirect:/Home/Home"; // Ana sayfaya yönlendirme
        } else {
            // Hata durumu
            model.addAttribute("globalErrors", List.of("Geçersiz e-posta veya şifre."));
            return "Home/Login"; // Tekrar login sayfasını döndür
        }
    }

    // Kullanıcıyı düz metin şifreyle kaydetmek için örnek metod
    @PostMapping("/register")
    public String registerUser(@RequestParam String email, @RequestParam String password) {
        Users newUser = new Users();
        newUser.setEmail(email);
        newUser.setPassword(password); // Şifreyi düz metin olarak kaydediyoruz
        userRepository.save(newUser);
        return "redirect:/login"; // Giriş sayfasına yönlendir
    }
}

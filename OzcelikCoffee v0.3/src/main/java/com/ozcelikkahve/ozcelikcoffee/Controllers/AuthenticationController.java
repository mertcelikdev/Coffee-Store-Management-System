/*package com.ozcelikkahve.ozcelikcoffee.Controllers;

import com.ozcelikkahve.ozcelikcoffee.Models.Users;
import com.ozcelikkahve.ozcelikcoffee.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthenticationController {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // PasswordEncoder'ı da enjekte edin
    @Autowired
    public AuthenticationController(UsersRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Login sayfasını görüntüleme
    @GetMapping("/login")
    public String showLoginForm() {
        return "Home/Login";  // Login sayfasını göster
    }

    // Giriş işlemi
    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, Model model) {
        Users user = userRepository.findByEmail(email);

        // Kullanıcı var mı ve şifre doğru mu kontrol et
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {  // Şifreyi BCrypt ile karşılaştır
            System.out.println("Giriş Başarılı");
            return "redirect:/home";  // Giriş başarılı ise anasayfaya yönlendir
        } else {
            // Hata durumu
            model.addAttribute("globalErrors", List.of("Geçersiz e-posta veya şifre."));
            System.out.println("Giriş Yanlış");
            return "Home/Login";  // Tekrar login sayfasına döndür
        }
    }
}*/

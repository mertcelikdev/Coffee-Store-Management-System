package com.ozcelikkahve.ozcelikcoffee.Controllers;

import com.ozcelikkahve.ozcelikcoffee.Models.Roles;
import com.ozcelikkahve.ozcelikcoffee.Models.Users;
import com.ozcelikkahve.ozcelikcoffee.Repositories.RolesRepository;
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
    private final RolesRepository rolesRepository;

    // PasswordEncoder'ı da enjekte edin
    @Autowired
    public AuthenticationController(UsersRepository userRepository, PasswordEncoder passwordEncoder, RolesRepository rolesRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
    }

    // Login sayfasını görüntüleme
    @GetMapping("/login")
    public String showLoginForm() {
        return "Home/Login";  // Login sayfasını göster
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        Users user = userRepository.findByEmail(email).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("user", user);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Geçersiz email veya şifre");
            return "Home/Login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        List<Roles> roles = rolesRepository.findAll();
        model.addAttribute("roles", roles);
        return "Home/Register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String email, @RequestParam String password) {
        // Şifreyi şifreleyin
        String encodedPassword = passwordEncoder.encode(password);

        // "User" rolünü veritabanından bulun
        Roles role = rolesRepository.findByName("user").orElseThrow(() -> new IllegalArgumentException("Geçersiz rol adı"));

        // Yeni kullanıcı oluşturun
        Users user = new Users();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole(role);

        // Kullanıcıyı veritabanına kaydedin
        userRepository.save(user);

        return "redirect:/login";  // Kullanıcıyı kaydettikten sonra login sayfasına yönlendir
    }
}


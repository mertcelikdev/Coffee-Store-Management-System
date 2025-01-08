package com.ozcelikkahve.ozcelikcoffee.Services;

import com.ozcelikkahve.ozcelikcoffee.Models.Users;
import com.ozcelikkahve.ozcelikcoffee.Repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordUpdateRunner implements CommandLineRunner {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordUpdateRunner(UsersRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Users> users = userRepository.findAll();
        for (Users user : users) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);
        }
    }
}
package com.ozcelikkahve.ozcelikcoffee.Security;
import com.ozcelikkahve.ozcelikcoffee.Models.Users;
import com.ozcelikkahve.ozcelikcoffee.Repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // UserRepository'inizi burada kullanın

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Veritabanından kullanıcıyı alın
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Kullanıcı bulunamadı");
        }

        // Kullanıcıyı UserDetails olarak döndürün
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                // Roller varsa ekleyebilirsiniz
                .build();
    }
}

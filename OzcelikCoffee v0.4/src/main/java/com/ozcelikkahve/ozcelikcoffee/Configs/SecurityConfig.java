/*package com.ozcelikkahve.ozcelikcoffee.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer -> configurer
                        // Admin rolündeki kullanıcılar her şeye erişebilsin
                        .requestMatchers("/**").permitAll()

                        // Employee rolündeki kullanıcılar kahve ve tatlı sayfalarına erişebilsin
                        .requestMatchers("/coffees/**", "/desserts/**").hasRole("employee")

                        // User rolündeki kullanıcılar sadece tatlı sayfasına erişebilsin
                        .requestMatchers("/desserts/**").hasRole("user")

                        // Diğer tüm talepler için kimlik doğrulaması yapılacak
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Giriş sayfası
                        .loginProcessingUrl("/authenticateTheUser")  // Giriş işlemi için URL
                        .defaultSuccessUrl("/home", true)  // Başarılı giriş sonrası yönlendirme
                        .permitAll()  // Giriş sayfasına herkesin erişebilmesi sağlanır
                );

        return http.build();
    }

    // BCryptPasswordEncoder kullanımı
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt ile şifreleme
    }
}*/

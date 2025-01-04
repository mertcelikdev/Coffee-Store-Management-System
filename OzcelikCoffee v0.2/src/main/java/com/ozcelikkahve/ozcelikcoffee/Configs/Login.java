package com.ozcelikkahve.ozcelikcoffee.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Login {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/admin/**").hasRole("admin")  // Admin rolüne sahip kullanıcılar için sadece admin sayfaları
                        .requestMatchers("/coffees/**").hasRole("employee")  // Employee rolüne sahip kullanıcılar için coffee sayfaları
                        .requestMatchers("/desserts/**").hasRole("user")  // User rolüne sahip kullanıcılar için desserts sayfaları
                        .anyRequest().authenticated()  // Diğer tüm talepler için kimlik doğrulaması yapılacak
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Giriş sayfası
                        .loginProcessingUrl("/authenticateTheUser")  // Giriş işlemini işleyen URL
                        .defaultSuccessUrl("/home", true)
                        .permitAll()  // Giriş sayfasına herkesin erişebilmesi sağlanır
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // Çıkış URL'si
                        .logoutSuccessUrl("/login")  // Çıkış sonrası login sayfasına yönlendirme
                        .permitAll()  // Çıkış yapabilmek için herkese izin
                );

        return http.build();
    }
}

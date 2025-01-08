package com.ozcelikkahve.ozcelikcoffee.Configs;

import com.ozcelikkahve.ozcelikcoffee.Repositories.UsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/coffees/**", "/desserts/**").hasRole("employee")
                        .requestMatchers("/desserts/**").hasRole("user")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
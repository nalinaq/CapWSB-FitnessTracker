package com.capgemini.wsb.fitnesstracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/v1/users/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/v1/users/**").hasRole("ADMIN")
                )
                .httpBasic(httpBasic -> {});
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = passwordEncoder();

        User.UserBuilder users = User.builder().passwordEncoder(encoder::encode);
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(users.username("user").password("some_user").roles("USER").build());
        manager.createUser(users.username("admin").password("some_admin").roles("ADMIN").build());

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Hashowanie haseł za pomocą BCrypt
    }
}

package com.example.kahvikauppa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Poistettu kehityskäyttöön
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // Mahdollista
                                                                                                  // H2-konsoli
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/admin").authenticated() // Vain admin vaatii kirjautumisen
                        .requestMatchers("/h2-console/**").permitAll() // H2-konsoli sallittu
                        .anyRequest().permitAll()) // Kaikki muu on vapaasti saatavilla
                .formLogin((form) -> form
                        .loginPage("/login") // Kirjautumissivu
                        .permitAll()
                        .defaultSuccessUrl("/admin", true)) // Onnistuneen kirjautumisen jälkeen admin-sivulle
                .logout((logout) -> logout.permitAll()); // Salli uloskirjautuminen

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        var user = org.springframework.security.core.userdetails.User
                .withUsername("1234")
                .password(encoder.encode("1234"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}

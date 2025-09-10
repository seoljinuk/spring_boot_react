package com.coffee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 강력한 해싱 함수
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 비활성화 (개발 환경용)
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // CORS 설정 적용 (CorsConfigurationSource Bean 사용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/member/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

package com.coffee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {

    // ======= Password Encoder Bean =======
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 강력한 해싱 함수
    }

    // ======= Spring Security 설정 =======
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 개발 환경에서 CSRF 비활성화
                .cors(cors -> {
                }) // CORS는 WebMvcConfigurer 혹은 CorsConfigurationSource Bean과 연결
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/member/**").permitAll() // 회원 관련 요청 허용
                        .anyRequest().authenticated() // 나머지 요청 인증 필요
                );

        return http.build();
    }

    // ======= 정적 리소스(이미지) 제공 =======
    @Configuration
    public static class WebConfig implements WebMvcConfigurer {

        @Value("${uploadPath}") // application.properties에서 읽어오기
        private String uploadPath;

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/images/**") // URL 요청 패턴
                    .addResourceLocations(uploadPath);  // 실제 이미지 경로
        }
    }
}

package com.coffee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // 비밀번호 암호화용 Bean 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security 설정
    /* 이 오류는 Spring Security 6.x부터 csrf() 메서드가 더 이상 직접 호출되지 않고, 대신 새로운 방식으로 설정해야 해서 발생하는 경고입니다.
즉, csrf().disable()가 deprecated 되었고, Spring Security 6.1+에서는 람다 기반 API로 바꿔야 합니다. */

    /*
    * .cors(cors -> {}) 한 줄만 추가하면 Spring Security가
WebMvcConfigurer.addCorsMappings()에서 설정한 값을 사용합니다.
    *
    * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // publicUrls는 무조건 허용하는 url 모음입니다.
        String[] publicUrls = {"/member/**", "/images/**", "/product/**", "/cart/**"};

        http
                .cors(cors -> {})  // ✅ CORS 활성화
                .csrf(csrf -> csrf.disable()) // ✅ 새로운 방식 (람다식)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicUrls).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable()
                )
                .httpBasic(httpBasic -> httpBasic.disable());
//                .logout(logout -> logout
//                        .logoutUrl("/member/logout")
//                        .logoutSuccessUrl("/")
//                        .permitAll()
//                );

        return http.build();
    }
}

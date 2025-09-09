package com.coffee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 해당 클래스는 자바에서 설정 파일로 인식합니다.
public class WebConfig implements WebMvcConfigurer {
    // WebMvcConfigurer : 웹 애플리케이션 설정용으로 사용하는 인터페이스

    // ✅ application.properties에서 경로 읽어오기
    @Value("${uploadPath}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/images"로 시작하는 요청을 받으면 productImageLocation에 지정된 폴더에서 이미지를 찾겠습니다.
        // registry.addResourceHandler("/images/**") // url 패턴(리액트 HomePage.js 파일과 연관)
        //        .addResourceLocations("file:///c:/boot/images/"); // 파일이 있는 실제 위치

        registry.addResourceHandler("/images/**") // url 패턴(리액트 HomePage.js 파일과 연관)
                .addResourceLocations(uploadPath); // 파일이 있는 실제 위치(application.properties에서 읽음)
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청에 CORS 적용
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
    }
}

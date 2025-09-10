package com.coffee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // application.properties에서 업로드 이미지 경로 읽어오기
    @Value("${uploadPath}")
    private String uploadPath;

    /**
     * "/images/**" 요청이 들어오면 실제 이미지가 저장된 경로(uploadPath)에서 파일을 제공
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로 허용
                .allowedOrigins("http://localhost:3000") // React dev 서버
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true) // 쿠키 전송 허용
                .maxAge(3600);
    }

//    /**
//     * React 등 외부 클라이언트에서 들어오는 요청에 대해 CORS 허용
//     * allowCredentials(true) 사용 시 allowedOrigins는 "*"가 아니라 특정 주소를 지정해야 함
//     */
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // 모든 요청에 대해 CORS 적용
//                        .allowedOrigins("http://localhost:3000") // React 개발 서버 주소
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                        .allowedHeaders("*")
//                        .allowCredentials(true); // 세션/쿠키 공유 허용
//            }
//        };
//    }
}

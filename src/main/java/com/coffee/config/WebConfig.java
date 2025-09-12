package com.coffee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // application.properties에서 업로드 이미지 경로 읽어오기
    @Value("${uploadPath}")
    private String uploadPath;

    /* "/images/**" 요청이 들어오면 실제 이미지가 저장된 경로(uploadPath)에서 파일을 제공  */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로 허용
                .allowedOrigins("http://localhost:3000") // React dev 서버
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowCredentials(true) // 쿠키 전송 허용
                .maxAge(3600);
    }

    /*
    ## 🔎 문제 원인
    1. **리액트 라우터는 클라이언트 사이드 라우팅** (CSR)
       * `navigate('/order/list?memberId=1')`로 이동하면 **브라우저 주소창만 바뀌고**
         실제로 서버(Spring Boot)에는 요청을 보내지 않아요.
       * React가 메모리에 있는 라우팅 테이블(AppRoutes.js)을 보고 OrderList 컴포넌트를 렌더링해 주는 것.

    2. **F5를 누르면 브라우저가 서버로 직접 요청**
       * `/order/list?memberId=1` 주소를 서버(Spring Boot)로 보냄
       * Spring Boot 서버 입장에서는 `/order/list` 라우트는 **REST API**이지 HTML 페이지가 아님
         ⇒ React 앱을 반환하지 않고 404(Not Found) 또는 JSON만 응답
       * 결국 React 앱이 실행되지 않아서 화면에 아무것도 안 보이게 됨.

    즉, **클라이언트 라우팅 경로를 서버가 모른다**는 게 원인입니다.
    ---

    ## 🛠 해결 방법
    ### ✅ 1. Spring Boot에서 React 앱의 라우트를 처리
    Spring Boot에서 React 앱의 모든 라우트를 `index.html`로 리다이렉트하도록 설정해야 합니다.

    */
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        // React 라우터가 처리해야 할 경로는 모두 index.html로 연결
//        registry.addViewController("/{spring:\\w+}").setViewName("forward:/index.html");
//
//        registry.addViewController("/**/{spring:\\w+}").setViewName("forward:/index.html");
//
//        registry.addViewController("/{spring:\\w+}/**{spring:?!(\\.js|\\.css)$}").setViewName("forward:/index.html");
//    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // React 라우터가 처리할 모든 경로를 index.html로 포워딩
        registry.addViewController("/{path:[^\\.]*}") // .js, .css, .png 등 제외
                .setViewName("forward:/index.html");
    }
}

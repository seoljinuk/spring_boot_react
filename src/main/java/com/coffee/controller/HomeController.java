package com.coffee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//Spring Boot 설정 (선택)
//React의 라우팅 기능을 제대로 활용하려면 Spring Boot에 다음과 같은 설정을 추가하여 React의 index.html을 기본으로 서빙하도록 해주는 것이 좋습니다:
@Controller
public class HomeController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }
}
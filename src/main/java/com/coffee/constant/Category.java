package com.coffee.constant;

// 상품의 카테고리 정보를 나타내기 위한 열거형 상수
public enum Category {
    BREAD("빵"),
    BEVERAGE("음료수"),
    CAKE("케익") ;

    private final String description ;

    Category(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

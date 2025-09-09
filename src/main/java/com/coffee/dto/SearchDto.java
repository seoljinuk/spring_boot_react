package com.coffee.dto;

import com.coffee.constant.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 상품에 대한 필드 검색을 위한 자바 클래스입니다.
@Getter @Setter @ToString
@AllArgsConstructor
public class SearchDto {
    // 조회할 범위를 선정하기 위한 변수로, 현재 시각과 상품 등록일을 비교하여 처리합니다.
    // all(전체 기간), 1d(하루), 1w(1주일), 1m(한개월), 6m(6개월)
    private String searchDateType ; // 콤보 박스

    private Category category ; // 특정 카테고리만 조회하겠습니다.(콤보 박스)

    // 상품 검색 모드(콤보 박스)_상품 이름(name) 또는 상품 설명(description)
    private String searchMode ;

    private String searchKeyword ; // 검색 키워드(입력 상자)


}

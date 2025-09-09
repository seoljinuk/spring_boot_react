package com.coffee.service;

import com.coffee.constant.Category;
import com.coffee.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductSpecification {

    /**
     * 특정 날짜 범위에 해당하는 상품들을 필터링하는 Specification입니다.
     *
     * @param searchDateType 검색할 날짜 범위를 나타내는 문자열
     * @return 해당 날짜 범위에 맞는 상품을 조회하기 위한 Specification
     */
    public static Specification<Product> hasDateRange(String searchDateType) {
        return (root, query, criteriaBuilder) -> {
            // 현재 날짜를 가져옵니다.
            LocalDate now = LocalDate.now();

            // 시작 날짜를 저장할 변수
            LocalDate startDate = null;

            // 날짜 범위에 따라 시작 날짜를 계산합니다.
            switch (searchDateType) {
                case "1d":
                    // 하루 전 날짜를 구합니다.
                    startDate = now.minus(1, ChronoUnit.DAYS);
                    break;
                case "1w":
                    // 일주일 전 날짜를 구합니다.
                    startDate = now.minus(1, ChronoUnit.WEEKS);
                    break;
                case "1m":
                    // 한 달 전 날짜를 구합니다.
                    startDate = now.minus(1, ChronoUnit.MONTHS);
                    break;
                case "6m":
                    // 6개월 전 날짜를 구합니다.
                    startDate = now.minus(6, ChronoUnit.MONTHS);
                    break;
                case "all":
                default:
                    // 전체 기간을 의미하는 경우, 날짜 범위 설정 없이 모든 상품을 조회하도록 합니다.
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true)); // 모든 상품을 조회
            }

            // 등록일(inputdate)이 계산된 startDate 이후인 상품만 선택
            return criteriaBuilder.greaterThanOrEqualTo(root.get("inputdate"), startDate);
        };
    }

    /**
     * 특정 카테고리에 속하는 상품들을 필터링하는 Specification입니다.
     *
     * @param category 검색할 카테고리 (예: BEVERAGE, BREAD, CAKE)
     * @return 해당 카테고리에 속하는 상품을 조회하기 위한 Specification
     */
    public static Specification<Product> hasCategory(Category category) {
        return (root, query, criteriaBuilder) ->
                // 카테고리가 주어진 category와 일치하는 상품들을 선택
                criteriaBuilder.equal(root.get("category"), category);
    }

    /**
     * 상품 이름에 특정 키워드가 포함된 상품들을 필터링하는 Specification입니다.
     *
     * @param keyword 상품 이름에 포함될 키워드
     * @return 상품 이름에 해당 키워드가 포함된 상품을 조회하기 위한 Specification
     */
    public static Specification<Product> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) ->
                // 상품 이름에 주어진 키워드가 포함된 상품을 선택
                criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
    }

    /**
     * 상품 설명에 특정 키워드가 포함된 상품들을 필터링하는 Specification입니다.
     *
     * @param keyword 상품 설명에 포함될 키워드
     * @return 상품 설명에 해당 키워드가 포함된 상품을 조회하기 위한 Specification
     */
    public static Specification<Product> hasDescriptionLike(String keyword) {
        return (root, query, criteriaBuilder) ->
                // 상품 설명에 주어진 키워드가 포함된 상품을 선택
                criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
    }
}

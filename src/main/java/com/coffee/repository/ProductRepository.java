package com.coffee.repository;

import com.coffee.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByIdDesc() ; // id를 기준으로 내림차순 정렬하기

    // id를 기준으로 내림차순 정렬한 이후 페이징 처리된 결과를 반환합니다.
    Page<Product> findAllByOrderByIdDesc(Pageable pageable) ;

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);
}

package com.coffee.service;

import com.coffee.dto.SearchDto;
import com.coffee.entity.Product;
import com.coffee.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository ;

    public List<Product> listProducts(){
        return this.productRepository.findAllByOrderByIdDesc() ;
    }

    public Page<Product> listProducts(Pageable pageable){
        return this.productRepository.findAllByOrderByIdDesc(pageable) ;
    }

//    public Page<Product> listProducts(SearchDto searchDto, Pageable pageable){
//        return this.productRepository.findAllByOrderByIdDesc(pageable) ;
//    }

    public Page<Product> listProducts(SearchDto searchDto, int pageNumber, int pageSize) {
        // Specification 인터페이스의 역할:
        // Specification은 엔티티 객체에 대한 쿼리 조건을 정의할 수 있는 조건자(Specification)로 사용됩니다.
        // 검색 조건에 맞는 상품을 찾는 조건을 빌드합니다.
        Specification<Product> spec = Specification.where(null);

        // 등록일 조건 추가 (예: all, 1d, 1w, 1m, 6m)
        if (searchDto.getSearchDateType() != null) {
            spec = spec.and(ProductSpecification.hasDateRange(searchDto.getSearchDateType()));
        }

        // 카테고리 조건 추가
        if (searchDto.getCategory() != null) {
            spec = spec.and(ProductSpecification.hasCategory(searchDto.getCategory()));
        }

        // 검색 모드에 따른 조건 추가 (상품 이름 또는 설명)
        if (searchDto.getSearchMode() != null && searchDto.getSearchKeyword() != null) {
            if ("name".equals(searchDto.getSearchMode())) {
                spec = spec.and(ProductSpecification.hasNameLike(searchDto.getSearchKeyword()));
            } else if ("description".equals(searchDto.getSearchMode())) {
                spec = spec.and(ProductSpecification.hasDescriptionLike(searchDto.getSearchKeyword()));
            }
        }

        // 상품 ID 역순 정렬을 포함한 Pageable 생성
        Sort sort = Sort.by(Sort.Order.desc("id"));

        // pageSize개씩 묶은 다음에 pageNumber 페이지의 내용을 추출합니다.(0 base)
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // 해당 조건에 맞는 상품 목록을 페이징하여 반환
        return this.productRepository.findAll(spec, pageable);
    }


    public Product getProductById(Long id) {
        Optional<Product> product = this.productRepository.findById(id);

        // 해당 상품이 존재하지 않으면 null 값을 반환합니다.
        return product.orElse(null) ;
    }

    public void save(Product product) {
        this.productRepository.save(product) ;
    }
}

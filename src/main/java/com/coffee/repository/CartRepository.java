package com.coffee.repository;

import com.coffee.entity.Cart;
import com.coffee.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // 특정 회원이 카트를 가지고 있는 지 확인합니다.(조회해서 없으면 생성)
    Optional<Cart> findByMember(Member member);
}

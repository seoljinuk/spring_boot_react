package com.coffee.repository;

import com.coffee.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<엔터티이름, 해당엔터티의기본키변수타입>
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일을 기준으로 회원을 찾습니다.
    Member findByEmail(String email);
}

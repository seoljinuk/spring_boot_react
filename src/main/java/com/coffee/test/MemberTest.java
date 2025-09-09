package com.coffee.test;

import com.coffee.constant.Role;
import com.coffee.entity.Member;
import com.coffee.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class MemberTest {
    @Autowired
    private MemberRepository memberRepository ;

    @Test
    public void insertMemberList(){
        // 회원 몇 명을 추가합니다.
        Member mem01 = new Member();
        mem01.setName("관리자");
        mem01.setEmail("admin@naver.com");
        mem01.setPassword("Admin@123");
        mem01.setAddress("마포구 공덕동");
        mem01.setRole(Role.ADMIN);
        mem01.setRegdate(LocalDate.now());
        memberRepository.save(mem01) ;
        System.out.println("----------------------");

        Member mem02 = new Member();
        mem02.setName("유영석");
        mem02.setEmail("bluesky@naver.com");
        mem02.setPassword("Bluesky@456");
        mem02.setAddress("용산구 이태원동");
        mem02.setRole(Role.USER);
        mem02.setRegdate(LocalDate.now());
        memberRepository.save(mem02) ;
        System.out.println("----------------------");

        Member mem03 = new Member();
        mem03.setName("곰돌이");
        mem03.setEmail("gomdori@naver.com");
        mem03.setPassword("Gomdori@789");
        mem03.setAddress("동대문구 휘경동");
        mem03.setRole(Role.USER);
        mem03.setRegdate(LocalDate.now());
        memberRepository.save(mem03) ;
        System.out.println("----------------------");
    }
}

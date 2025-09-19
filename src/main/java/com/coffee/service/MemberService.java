package com.coffee.service;

import com.coffee.entity.Member;
import com.coffee.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // final 키워드에 대하여 자동으로 객체 주입을 해줍니다.
public class MemberService {
    private final MemberRepository memberRepository ;

    public Member findByEmail(String email){
        return this.memberRepository.findByEmail(email);
    }
	
	@Override
    @Transactional
    public Member signup(Member member) {
        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(Role.USER);
        member.setRegdate(LocalDate.now());

        return memberRepository.save(member);
    }
}

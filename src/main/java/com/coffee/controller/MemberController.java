package com.coffee.controller;

import com.coffee.constant.Role;
import com.coffee.entity.Member;
import com.coffee.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import com.coffee.service.MemberService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController // 웹 요청을 처리해주는 컨트롤러로 만들어 줍니다.
@RequiredArgsConstructor
@RequestMapping("/member")
//@CrossOrigin(origins =  "http://localhost:3000") // 3000번 포트에서 들어 오는 데이터를 위하여 개방하겠습니다.
public class MemberController {

    private final MemberService memberService;
//    private final PasswordEncoder passwordEncoder; // Lombok이 생성자 주입 처리

    // 데이터 베이스와 연동하는 repository 객체
    private final MemberRepository memberRepository ;

    /*
    컨트롤러 메소드 : 클라이언트의 요청에 대한 응답을 해주는 메소드
    컨트롤러 메소드의 매개 변수에 어떠한 것이 사용되는 지 공부해 주세요.
    */



    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Member bean, HttpServletRequest request){
        // bean : 클라이언트에서 넘겨준 로그인 정보를 담고 있는 객체
        // member : 데이터 베이스에서 내가 조회한 회원 정보 객체
        Member member = this.memberService.findByEmail(bean.getEmail()) ;

        // response : 클라이언트에게 넘겨주고자 하는 응답 정보의 모음
        Map<String, Object> response = new HashMap<String, Object>();

        if(member == null){
            response.clear();
            response.put("message", "해당 이메일이 존재하지 않습니다.");
            // 응답 코드 401 : Unauthorized(인증 실패)
            return ResponseEntity.status(401).body(response);
        }

        boolean isCorrectPassword = false ;
//        isCorrectPassword = member.getPassword().equals(bean.getPassword());
        // 🔑 수정된 비밀번호 비교
        isCorrectPassword = passwordEncoder.matches(bean.getPassword(), member.getPassword());

        //  if(!isCorrectPassword){}
        if(isCorrectPassword == false){
            response.clear();
            response.put("message", "비밀 번호가 일치하지 않습니다.");
            return ResponseEntity.status(401).body(response);
        }

        // 로그인 성공시 session 영역에 로그인 정보 바인딩
        HttpSession session = request.getSession() ;
        session.setAttribute("user", member);

        response.clear();
        response.put("message", "로그인 성공");
        response.put("member", member);
        return ResponseEntity.ok(response) ;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        // 현재 요청에 대한 세션이 존재하면 반환하고, 없으면 null을 반환합니다.
        HttpSession session = request.getSession(false);

        if(session != null){
            session.invalidate(); // 세션의 내용을 모두 제거하기
        }

        return ResponseEntity.ok("로그 아웃 성공") ;
    }


    private final PasswordEncoder passwordEncoder;
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody Member member, BindingResult bindingResult){
        // ResponseEntity : HTTP 응답 코드, 메시지 등을 표현하는 클래스
        // @RequestBody : JSON 형태의 문자열을 자바의 객체 형식으로 변환해 줍니다.
        // @Valid : 유효성 검사를 수행하는 어노테이션입니다.
        // BindingResult : 유효성 검사시 문제가 있으면 예외를 발생시켜 줍니다.

        System.out.println("오류 갯수");
        System.out.println(bindingResult.getFieldErrorCount());

        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<String, String>() ;
            for(FieldError err : bindingResult.getFieldErrors()){
                errors.put(err.getField(), err.getDefaultMessage());
            }
            System.out.println(errors);
            // HttpStatus.BAD_REQUEST : 사용자가 잘못된 형식의 요청이 들어 오는 경우에 많이 사용합니다.
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // 이메일 중복 체크
        if (memberRepository.findByEmail(member.getEmail()) != null) {
            return new ResponseEntity<>(Map.of("email", "이미 존재하는 이메일입니다."), HttpStatus.BAD_REQUEST);
        }

        // 회원 가입 처리
        // 비밀번호 암호화 적용 ✅
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole(Role.USER); // 일반 사용자
        member.setRegdate(LocalDate.now()); // 현재 시각
        
        System.out.println("member");
        System.out.println(member);

        // 데이터 베이스에 저장하기
        memberRepository.save(member);

        return new ResponseEntity<>("회원 가입 성공", HttpStatus.CREATED) ;
    }

    @GetMapping("/one") // http://localhost:9000/member/one
    public Member oneData(){
        Member bean = new Member() ;
        bean.setAddress("마포");
        bean.setId(0L);
        bean.setName("김철수");
        bean.setEmail("kim@example.com");
        bean.setPassword("1234");
        bean.setRole(Role.USER);
        bean.setRegdate(null);
        return bean ;
    }

}

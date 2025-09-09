package com.coffee.entity;

import com.coffee.constant.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

// 회원 1명을 의미하는 자바 클래스
@Getter
@Setter
@ToString
@Entity // 이 클래스를 엔터티로 인식하세요.
@Table(name="members") // 테이블 생성시 이름은 members로 작성해 주세요.
public class Member {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name ;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "올바른 이메일 형식으로 입력해 주셔야 합니다.")
    private String email ;

    @NotBlank(message = "비밀 번호는 필수 입력 사항입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 8자리 이상, 16자리 이하로 입력해 주세요.")
    @Pattern(regexp = ".*[A-Z].*", message = "비밀 번호는 대문자 1개 이상을 포함해야 합니다.")
    @Pattern(regexp = ".*[!@#$%].*", message = "비밀 번호는 특수 문자 '!@#$%' 중 하나 이상을 포함해야 합니다.")
    private String password ;

    @NotBlank(message = "주소는 필수 입력 사항입니다.")
    private String address ;

    @Enumerated(EnumType.STRING)
    private Role role ; // 관리자 or 일반 사용자
    private LocalDate regdate ; // 등록 일자
}

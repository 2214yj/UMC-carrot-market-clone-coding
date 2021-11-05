package com.example.demo.src.user.model;

import lombok.*;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(userIdx, nickname, email, password)를 받는 생성자를 생성
/**
 * Res.java: From Server To Client
 * 하나의 회원정보 조회 요청(Get Request)의 결과(Respone)를 보여주는 데이터의 형태
 */
public class User {
    private int userIdx;
    private String email;
    private String password;
    private String nickname;
}

package com.example.demo.src.user.model;

import lombok.*;



/**
 * Res.java: From Server To Client
 * 하나의 회원정보 조회 요청(Get Request)의 결과(Respone)를 보여주는 데이터의 형태
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    private int userIdx;
    private String email;
    private String password;
    private String nickname;
}

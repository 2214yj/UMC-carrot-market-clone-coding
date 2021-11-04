package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/**
 * Res.java: From Server To Client
 * 회원정보 조회 요청(Get Request)의 결과(Respone)를 보여주는 데이터의 형태
 */
@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int userIdx;
    private String nickname;
    private String email;
    private String password;
}

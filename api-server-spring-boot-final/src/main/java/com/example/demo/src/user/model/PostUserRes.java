package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Res.java: From Server To Client
 * 회원가입의 결과(Respone)를 보여주는 데이터의 형태
 */
@Getter
@Setter
@AllArgsConstructor
public class PostUserRes {
    private int userIdx;
//    해당 부분은 7주차 - JWT 수업 후 주석해제 및 대체해주세요!
//    private String jwt;
}

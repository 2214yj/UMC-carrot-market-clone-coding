package com.example.demo.src.user.model;

import lombok.*;

/**
 * Req.java: From Client To Server
 * 회원가입을 하기 위해 생성 요청(Post Request)을 하기 위해 서버에 전달할 데이터의 형태
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostUserReq {
    private String email;
    private String password;
    private String nickname;
}

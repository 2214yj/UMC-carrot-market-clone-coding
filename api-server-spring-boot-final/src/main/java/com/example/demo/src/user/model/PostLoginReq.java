package com.example.demo.src.user.model;

import lombok.*;

/**
 * Req.java: From Client To Server
 * 로그인을 위해 서버에 전달할 데이터의 형태
 * Email, Password 정보를 전달하기 위해 Body값까지 전달하는 Post 요청을 사용한다.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLoginReq {
    private String email;
    private String password;
}

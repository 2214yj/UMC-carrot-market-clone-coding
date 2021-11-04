package com.example.demo.src.user.model;

import lombok.*;


/**
 * Req.java: From Client To Server
 * 회원정보 수정 요청(Patch Request)을 하기 위해 서버에 전달할 데이터의 형태
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchUserReq {
    private int userIdx;
    private String nickname;
}

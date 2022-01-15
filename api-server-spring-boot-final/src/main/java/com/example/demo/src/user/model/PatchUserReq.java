package com.example.demo.src.user.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@RequiredArgsConstructor // 해당 클래스의 모든 멤버 변수(userIdx, nickname)를 받는 생성자를 생성
/**
 * Req.java: From Client To Server
 * 회원정보 수정 요청(Patch Request)을 하기 위해 서버에 전달할 데이터의 형태
 */
public class PatchUserReq {
    private int userIdx;

    @NotBlank(message = "nickname cannot be blank. ")
    private String nickname;

    @NotBlank(message = "nickname cannot be blank. ")
    private String address;

    @Size(min = 6, max = 12, message = "password can be only 6 to 12 size. ")
    private String password;

    public PatchUserReq(int userIdx, String nickname, String address) {
        this.userIdx = userIdx;
        this.nickname = nickname;
        this.address = address;
    }

    public PatchUserReq(int userIdx, String password) {
        this.userIdx = userIdx;
        this.password = password;
    }
}

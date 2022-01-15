package com.example.demo.src.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(email, password, nickname, profileImage)를 받는 생성자를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 해당 클래스의 파라미터가 없는 생성자를 생성, 접근제한자를 PROTECTED로 설정.
public class PostUserReq {
    @Email(message = "please keep email format. ")
    private String email;

    @Size(min = 6, max = 12, message = "password can be only 6 to 12 size. ")
    private String password;

    @NotBlank(message = "nickname cannot be blank. ")
    private String nickname;

    @NotBlank(message = "address cannot be blank. ")
    private String address;

    private String image;
}

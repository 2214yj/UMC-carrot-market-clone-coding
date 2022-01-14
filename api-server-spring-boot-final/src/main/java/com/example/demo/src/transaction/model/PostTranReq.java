package com.example.demo.src.transaction.model;

import com.example.demo.src.entity.SellStatus;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(email, password, nickname, profileImage)를 받는 생성자를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 해당 클래스의 파라미터가 없는 생성자를 생성, 접근제한자를 PROTECTED로 설정.
public class PostTranReq {
    @NotEmpty(message = "message cannot be empty")
    private String title;

    @NotEmpty(message = "content cannot be empty")
    private String content;

    @NotEmpty(message = "content cannot be empty")
    private String itemName;

    @Min(value = 0,message = "0원 이상만")
    private String price;

    @NotEmpty(message = "category cannot be empty")
    private String category;

    private String repImg;

    @NotEmpty(message = "address cannot be empty")
    private String address;

    private List<String> image;
}

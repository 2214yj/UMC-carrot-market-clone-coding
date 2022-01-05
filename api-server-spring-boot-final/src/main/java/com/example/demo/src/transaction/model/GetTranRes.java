package com.example.demo.src.transaction.model;

import com.example.demo.src.entity.SellStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(email, password, nickname, profileImage)를 받는 생성자를 생성
public class GetTranRes {
    private String title;
    private String content;
    private String itemName;
    private String price;
    private String category;
    private List<String> image;
    private String sellStatus;
    private String createdAt;
    private String nickname;
    private String address;
    private String userImage;
    private Page<Comment> commentPage;

    public GetTranRes(String title, String content, String item_name, String price, String category, String address,String sell_status, String created_at) {
        this.title = title;
        this.content = content;
        this.itemName = item_name;
        this.price = price;
        this.category = category;
        this.address = address;
        this.sellStatus = sell_status;
        this.createdAt = created_at;
    }

    public GetTranRes(String nickname, String image) {
        this.nickname = nickname;
        this.userImage = image;
    }


}

package com.example.demo.src.transaction.model;

import com.example.demo.src.entity.SellStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(emai
public class PutTranReq {
    private String title;
    private String content;
    private String itemName;
    private String price;
    private String category;
    private String repImg;
    private String address;
    private String sellStatus;
    private List<String> image;
}

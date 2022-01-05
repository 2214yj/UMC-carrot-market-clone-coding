package com.example.demo.src.transaction.model;

import com.example.demo.src.entity.PostEntity;
import com.example.demo.src.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor
public class Comment {
    private int id;

    private String content;

    private String userNick;

    private String created_At;
}

package com.example.demo.src.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLikeRes {
    private int transactionId;
    private int likeTotalCount;

    public PostLikeRes(int transactionId) {
        this.transactionId = transactionId;
    }
}

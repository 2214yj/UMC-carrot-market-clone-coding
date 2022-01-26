package com.example.demo.src.transaction.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class GetSearchTranRes {
    @JsonProperty
    private int transactionId;
    @JsonProperty
    private String title;
    @JsonProperty
    private String price;
    @JsonProperty
    private String category;
    @JsonProperty
    private String repImg;
    @JsonProperty
    private String sellStatus;
    @JsonProperty
    private String createdAt;
    @JsonProperty
    private String address;
    @JsonProperty
    private int likeTotalCount;

    public GetSearchTranRes(int id, String title, String price, String category, String rep_img, String sell_status, String created_at, String address) {

        this.transactionId = id;
        this.title = title;
        this.price = price;
        this.category = category;
        this.repImg = rep_img;
        this. sellStatus = sell_status;
        this.createdAt = created_at;
        this. address = address;
    }
}

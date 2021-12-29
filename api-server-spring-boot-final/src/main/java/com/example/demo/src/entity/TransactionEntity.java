package com.example.demo.src.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
@Table(name = "transaction")
public class TransactionEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int id;

    @Column(length = 500)
    private String title;

    @Column(length = 4000)
    private String content;

    private String itemName;

    private String price;

    private String category;

    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus;

    private String repImg;

    private String address;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity User;

}

package com.example.demo.src.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
@Table(name = "user")
public class UserEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userIdx;

    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    private String nickname;

    private String address;

    private String image;

}

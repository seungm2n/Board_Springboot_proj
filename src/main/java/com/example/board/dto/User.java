package com.example.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class User {
    private int userId;
    private String email;
    private String name;
    private String password;
    private LocalDateTime regdate; // 원래 날짜는 type으로 읽어온 후 문자열로 변환
}

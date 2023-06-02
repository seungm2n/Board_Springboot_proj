package com.example.board.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Board {
    private int boardId;
    private String title;
    private String content;
    private int userId;
    private String name;
    private LocalDateTime regdate;
    private int viewCnt;
}
package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// HTTP요청을 받아 응답하는 컴포넌트. 스프링부트가 자동으로 Bean 생성함
@Controller
public class BoardController {
    // 게시물 목록을 보여줌
    // 컨트롤러의 메소드가 리턴하는 문자열은 템플릿
    // http://localhost:8080/ ===> "list"라는 이름의 템플릿을 사용하여 화면에 출력.(forwarding)
    // list를 리턴한다는 것은
    // classpath:/templates/list.html
    @GetMapping("/")
    public String list() {
        // 게시물 목록 읽어오고, 페이징 처리
        return "list";
    }

    // /board?id=3 : 파라미터 id,  파마미터 id의 값은 3
    // /board?id=2
    // /board?id=1
    @GetMapping("/board")
    public String board(@RequestParam("id") int id) {
        System.out.println("id : " + id);

        // id에 해당하는 게시물을 읽어옴
        // id에 해당하는 게시물의 조회수도 1 증가

        return "board";
    }

    // 삭제. 관리자는 모든 글 삭제 가능
    // 수정.
}

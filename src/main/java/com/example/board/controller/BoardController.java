package com.example.board.controller;

import com.example.board.dto.LoginInfo;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

// HTTP요청을 받아 응답하는 컴포넌트. 스프링부트가 자동으로 Bean 생성함
@Controller
public class BoardController {
    // 게시물 목록을 보여줌
    // 컨트롤러의 메소드가 리턴하는 문자열은 템플릿
    // http://localhost:8080/ ===> "list"라는 이름의 템플릿을 사용하여 화면에 출력.(forwarding)
    // list를 리턴한다는 것은
    // classpath:/templates/list.html
    @GetMapping("/")
    public String list(HttpSession session, Model model) {
        // 게시물 목록 읽어오고, 페이징 처리
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        model.addAttribute("loginInfo",loginInfo); // 템플릿에게

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

    @GetMapping("/writeForm")
    public String writeForm() {
        // 로그인한 사용자만 글을 쓸 수 있음.
        // 세션에서 로그인한 정보를 읽어드림. 로그인 하지 않았다면 리스트보기로 자동 이동

        return "writeForm";
    }

    @PostMapping("/write")
    public String write(
            @RequestParam("title") String title,
            @RequestParam("content") String content
    ) {
        // 로그인한 사용자만 글을 쓸 수 있음.
        // 세션에서 로그인한 정보를 읽어드림. 로그인 하지 않았다면 리스트보기로 자동 이동
        System.out.println("title : " + title);
        System.out.println("content : " + content);
        // 로그인한 회원 정보 + 제목, 내용을 저장

        return "redirect:/"; // 리스트보기로 리다이렉트
    }
}

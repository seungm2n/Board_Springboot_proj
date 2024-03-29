package com.example.board.controller;

import com.example.board.dto.Board;
import com.example.board.dto.LoginInfo;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

// HTTP요청을 받아 응답하는 컴포넌트. 스프링부트가 자동으로 Bean 생성함
@Controller
@RequiredArgsConstructor // 생성자에서 받아서 자동으로 초기화 해줌.
public class BoardController {

    private final BoardService boardService;
    // 게시물 목록을 보여줌
    // 컨트롤러의 메소드가 리턴하는 문자열은 템플릿
    // http://localhost:8080/ ===> "list"라는 이름의 템플릿을 사용하여 화면에 출력.(forwarding)
    // list를 리턴한다는 것은
    // classpath:/templates/list.html
    @GetMapping("/")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model m) {
        // 게시물 목록 읽어오고, 페이징 처리
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        m.addAttribute("loginInfo",loginInfo); // 템플릿에게

        int totalCount = boardService.getTotalCount();
        List<Board> list = boardService.getBoards(page); // page가 1,2,3,4 ...
        int pageCount = totalCount / 10; // 1
        if(totalCount % 10 > 0) {   // 나머지가 있을 경우 1page 추가
            pageCount++;
        }
        int currentPage = page;

        m.addAttribute("list",list);
        m.addAttribute("pageCount",pageCount);
        m.addAttribute("currentPage",currentPage);
        return "list";
    }

    // /board?id=3 : 파라미터 id,  파마미터 id의 값은 3
    // /board?id=2
    // /board?id=1
    @GetMapping("/board")
    public String board(@RequestParam("boardId") int boardId, Model m) {
        System.out.println("boardId : " + boardId);

        // id에 해당하는 게시물을 읽어옴
        // id에 해당하는 게시물의 조회수도 1 증가
        Board board = boardService.getBoard(boardId);
        m.addAttribute("board", board);

        return "board";
    }

    // 삭제. 관리자는 모든 글 삭제 가능
    // 수정.
    @GetMapping("/writeForm")
    public String writeForm(HttpSession session, Model m) {
        // 로그인한 사용자만 글을 쓸 수 있음.
        // 세션에서 로그인한 정보를 읽어드림. 로그인 하지 않았다면 리스트보기로 자동 이동
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if(loginInfo == null) { // 세션에 로그인 정보가 없으면 로그인폼으로 redirect함.
            return "redirect:/loginform";
        }
        m.addAttribute("loginInfo",loginInfo);

        return "writeForm";
    }

    @PostMapping("/write")
    public String write(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            HttpSession session
    ) {
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if(loginInfo == null) { // 세션에 로그인 정보가 없으면 로그인폼으로 redirect함.
            return "redirect:/loginform";
        }
        // 로그인한 사용자만 글을 쓸 수 있음.
        // 세션에서 로그인한 정보를 읽어드림. 로그인 하지 않았다면 리스트보기로 자동 이동
        System.out.println("title : " + title);
        System.out.println("content : " + content);
        // 로그인한 회원 정보 + 제목, 내용을 저장

        boardService.addBoard(loginInfo.getUserId(), title, content);

        return "redirect:/"; // 리스트보기로 리다이렉트
    }

    @GetMapping("/delete")
    public String delete(
            @RequestParam("boardId") int boardId,
            HttpSession session
    ){
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if(loginInfo == null) { // 세션에 로그인 정보가 없으면 로그인폼으로 redirect함.
            return "redirect:/loginform";
        }
        // 작성자와 로그인한 사용자 ID가 같은가?
        List<String> roles = loginInfo.getRoles();
        if(roles.contains("ROLE_ADMIN")) {
            boardService.deleteBoard(boardId);
        } else {
        boardService.deleteBoard(loginInfo.getUserId(), boardId);
        }
        return "redirect:/";
    }

    @GetMapping("/updateform")
    public String updateform(@RequestParam("boardId") int boardId, Model m, HttpSession session) {
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if(loginInfo == null) { // 세션에 로그인 정보가 없으면 로그인폼으로 redirect함.
            return "redirect:/loginform";
        }
        // boardId에 해당하는 정보를 읽어와서 updateform에 전달함
        Board board = boardService.getBoard(boardId, false);
        m.addAttribute("board", board);
        m.addAttribute("loginInfo", loginInfo);

        return "updateform";
    }

    @PostMapping("/update")
    public String update(@RequestParam("boardId") int boardId,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content,
                         HttpSession session
                         ){
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if(loginInfo == null) { // 세션에 로그인 정보가 없으면 로그인폼으로 redirect함.
            return "redirect:/loginform";
        }
        // boardId에 해당하는 글의 제목과 내용을 수정.
        // 단, 글쓴이만.
        Board board = boardService.getBoard(boardId, false);
        if (board.getUserId() != loginInfo.getUserId()) {
            return "redirect:/board?boardId="+boardId;
        }

        boardService.updateBoard(boardId, title, content);
        return "redirect:/board?boardId="+boardId;
    }
}

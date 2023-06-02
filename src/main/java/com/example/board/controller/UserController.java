package com.example.board.controller;

import com.example.board.dto.LoginInfo;
import com.example.board.dto.User;
import com.example.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // http://localhost:8080/userRegForm
    // classpath:/templates/userRegForm.html
    @GetMapping("/userRegForm")
    public String UserRegForm() {
        return "userRegForm";
    }

    /**
     * 회원 정보를 등록함.
     *
     * @param name
     * @param email
     * @param password
     * @return
     */
    @PostMapping("/userReg")
    public String UserReg(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        System.out.println("name : " + name);
        System.out.println("email : " + email);
        System.out.println("password : " + password);

        userService.addUser(name, email, password);
        // 회원 정보를 저장

        return "redirect:/welcome"; // 브라우저에게 자동으로 http://localhost:8080/welcome으로 이동
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/loginform")
    public String loginform() {
        return "loginform";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session // Spring이 자동으로 session을 처리하는 HttpSession 객체를 넣어줌.
    ) {
        // email에 해당하는 회원 정보를 읽어 온 후
        // 아이디 암호가 맞다면 세션에 회원정보를 저장
        System.out.println("email : " + email);
        System.out.println("password : " + password);

        try {
            User user = userService.getUser(email);
            if(user.getPassword().equals(password)) {
                System.out.println("암호가 같습니다.");
                LoginInfo loginInfo = new LoginInfo(user.getUserId(), user.getEmail(), user.getName());

                // 권한정보를 읽어와서 loginInfo에 추가한다
                List<String> roles = userService.getRoles(user.getUserId());
                loginInfo.setRoles(roles);

                session.setAttribute("loginInfo", loginInfo);
                System.out.println("세션에 로그인 정보 저장");
            } else {
                throw new RuntimeException("암호가 같지 않습니다.");
            }
        } catch (Exception e) {
            System.out.println("로그인에 실패하였습니다.");
            return "redirect:/loginform?error=true";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션에서 회원정보 제거
        session.removeAttribute("loginInfo");
        return "redirect:/";
    }
}

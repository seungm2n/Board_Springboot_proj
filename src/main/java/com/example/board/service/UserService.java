package com.example.board.service;

import com.example.board.dao.UserDao;
import com.example.board.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 트랜잭션 단위로 실행될 메소드를 선언하고 있는 클래스
// 스프링이 관리하는 Bean
@Service
@RequiredArgsConstructor // lombok이 final 필드를 초기화하는 생성자를 자동으로 생성함
public class UserService {

    private final UserDao userDao;

    // 보통 서비스에서는 @Transactional을 붙여서 하나의 트랜잭션을 처리함.
    // Spring Boot는 트랜잭션을 처리해주는 트랜잭션 관리자를 가지고 있음
    @Transactional
    public User addUser(String name, String email, String password) {

        // 트랜잭션이 시작됨
        User user = userDao.addUser(name, email, password);
        userDao.mappingUserRole(user.getUserId()); // 권한 부여

        return user;
    }
}

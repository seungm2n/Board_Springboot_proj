package com.example.board.dao;

import com.example.board.dto.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// 스프링이 관리하는 Bean
@Repository
public class UserDao {
    // Spring JDBC를 이용한 코드.
    @Transactional
    public User addUser(String email, String name, String password) {
        // Service에서 이미 트랜잭션이 시작했기 때문에, 그 트랜잭션에 포함.

        return null;
    }

    public int getLastInsertId() {
        return 0;
    }

    public void mappingUserRole(int userId) {

    }
}

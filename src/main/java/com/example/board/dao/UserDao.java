package com.example.board.dao;

import com.example.board.dto.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// 스프링이 관리하는 Bean
@Repository
public class UserDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private SimpleJdbcInsertOperations insertUser;

    public UserDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("user")
                .usingGeneratedKeyColumns("user_id");
    }
    // Spring JDBC를 이용한 코드.
    @Transactional
    public User addUser(String name, String email, String password) {
        // Service에서 이미 트랜잭션이 시작했기 때문에, 그 트랜잭션에 포함.
        User user = new User();

        // insert into user (name, email, password, regdate) values (:email, :name, :password, :regdate);
        user.setName(name); // name 칼럼
        user.setEmail(email);   // email
        user.setPassword(password); // password
        user.setRegdate(LocalDateTime.now());   // regdate

        SqlParameterSource params = new BeanPropertySqlParameterSource(user);
        System.out.println(params);
        Number number = insertUser.executeAndReturnKey(params); // insert를 실행하고 자동으로 생성된 id를 가져옴

        int userId = number.intValue();
        user.setUserId(userId);

        return user;
    }

//    public int getLastInsertId() {
//        return 0;
//    }

    public void mappingUserRole(int userId) {
        // Service에서 이미 트랜잭션이 시작했기 때문에, 그 트랜잭션에 포함.
        String sql = "insert into user_role(user_id, role_id) values (:userId, 1)";
        SqlParameterSource params = new MapSqlParameterSource("userId", userId);
        jdbcTemplate.update(sql, params);
    }

    @Transactional
    public User getUser(String email) {
        try {
            // user_id => setUserId, email => setEmail ...
            String sql = "select user_id, email, name, password, regdate from user where email = :email";
            SqlParameterSource params = new MapSqlParameterSource("email", email);
            RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);
            User user = jdbcTemplate.queryForObject(sql, params, rowMapper);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<String> getRoles(int userId) {
        String sql = "SELECT r.name FROM USER_ROLE ur, ROLE r\n" +
                    "WHERE ur.role_id= r.role_id\n" +
                    "AND ur.user_id=:userId";
        List<String> roles = jdbcTemplate.query(sql, Map.of("userId",userId), (rs, rowNum) -> {
           return rs.getString(1);
        });
        return roles;
    }
}
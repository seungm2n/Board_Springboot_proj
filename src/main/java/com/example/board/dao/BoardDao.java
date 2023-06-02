package com.example.board.dao;

import com.example.board.dto.Board;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class BoardDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations insertBoard;

    // 생성자 주입. 스프링이 자동으로 HikariCP Baen을 주입
    public BoardDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        insertBoard = new SimpleJdbcInsert(dataSource)
                .withTableName("board")
                .usingGeneratedKeyColumns("board_id");
    }

    @Transactional
    public void addBoard(int userId, String title, String content) {
        Board board = new Board();
        board.setUserId(userId);
        board.setTitle(title);
        board.setContent(content);
        SqlParameterSource params = new BeanPropertySqlParameterSource(board);
        insertBoard.execute(params);
    }

    @Transactional(readOnly = true)
    public int getTotalCount() {
        String sql = "SELECT count(*) as totalCount FROM board";
        Integer totalCount = jdbcTemplate.queryForObject(sql, Map.of(), Integer.class);
        return totalCount.intValue();
    }

    @Transactional(readOnly = true)
    public List<Board> getBoards(int page) {
        int start = (page - 1) * 10;
        String sql = "SELECT u.user_id, b.board_id, b.title, b.regdate, b.view_cnt, u.name FROM board b, USER u WHERE b.user_id = u.user_id ORDER BY regdate DESC LIMIT :start,10";
        RowMapper<Board> rowMapper = BeanPropertyRowMapper.newInstance(Board.class);
        List<Board> list = jdbcTemplate.query(sql, Map.of("start", start), rowMapper);
        return list;
    }

    @Transactional(readOnly = true)
    public Board getBoard(int boardId) {
        String sql = "SELECT u.user_id, b.board_id, b.title, b.regdate, b.view_cnt, u.name, b.content FROM board b, USER u WHERE b.user_id = u.user_id AND b.board_id = :boardId";
        RowMapper<Board> rowMapper = BeanPropertyRowMapper.newInstance(Board.class);
        Board board = jdbcTemplate.queryForObject(sql, Map.of("boardId", boardId), rowMapper);
        return board;
    }

    @Transactional
    public void updateViewCnt(int boardId) {
        String sql = "UPDATE board SET view_cnt = VIEW_cnt + 1 WHERE board_id = :boardId";
        jdbcTemplate.update(sql, Map.of("boardId", boardId));

    }

    @Transactional
    public void deleteBoard(int boardId) {
        String sql = "delete from board where board_id = :boardId";
        jdbcTemplate.update(sql, Map.of("boardId", boardId));
    }

    @Transactional
    public void updateBoard(int boardId, String title, String content) {
        String sql = "update board set title = :title, content= :content where board_id = :boardId";

        Board board = new Board();

        board.setBoardId(boardId);
        board.setTitle(title);
        board.setContent(content);

        SqlParameterSource params = new BeanPropertySqlParameterSource(board);
        jdbcTemplate.update(sql, params);
    }
}

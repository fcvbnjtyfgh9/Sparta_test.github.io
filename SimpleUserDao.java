package com.example.demo.Dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class SimpleUserDao {

    private final JdbcTemplate jdbcTemplate;

    public SimpleUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int InsertUserInfo(Map<String, String> params) {
        // SQL 쿼리 예시
        String sql = "INSERT INTO users (username, password, enabled, realname) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, params.get("user_id"), params.get("user_password"), 1, params.get("user_realname"));
    }

    public int InsertAuthorityInfo(String username) {
        // SQL 쿼리 예시
        String sql = "INSERT INTO authorities (username, role) VALUES (?, ?)";
        return jdbcTemplate.update(sql, username, "ROLE_USER");
    }
}
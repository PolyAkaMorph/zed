package com.zed.repository;

import com.zed.dto.PersonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PreparedStatement {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public String getTest() {
        String sql = "select id, test_str from test";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        return maps.stream().map(e -> e.get("test_str").toString()).collect(Collectors.joining());
    }

    public Integer createNewUser(String login, String password) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("user_id").usingColumns("login","password_hash");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("login", login);
        parameters.put("password_hash", password);
        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)).intValue();
    }

    public void createNewPersonal(PersonInfo personInfo, Integer newUserId) {
        String insertSql = "insert into person (user_id, name, surname, age, sex, interests, city) values (?,?,?,?,?,?,?);";
        jdbcTemplate.update(insertSql, newUserId,
                personInfo.getName(), personInfo.getSurname(), personInfo.getAge(), personInfo.getSex(), personInfo.getInterests(), personInfo.getCity());
    }
}

package com.zed.repository;

import com.zed.dto.PersonInfo;
import com.zed.dto.RegistrationInfo;
import com.zed.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Integer createNewUser(String login, String password) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("user_id").usingColumns("login", "password_hash");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("login", login);
        parameters.put("password_hash", password);
        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)).intValue();
    }

    public void createNewPersonal(PersonInfo personInfo, Integer newUserId) {
        String sql = "insert into person (user_id, name, surname, age, sex, interests, city) values (?,?,?,?,?,?,?);";
        jdbcTemplate.update(sql, newUserId,
                personInfo.getName(), personInfo.getSurname(), personInfo.getAge(), personInfo.getSex(), personInfo.getInterests(), personInfo.getCity());
    }

    public Integer getUserID(String login) {
        String sql = "select u.user_id from user u where u.login = :login";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("login", login);
        return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Integer.class);
    }

    public Integer getPersonID(String login) {
        String sql = "select p.person_id from user u join person p on p.user_id = u.user_id where u.login = :login";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("login", login);
        return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Integer.class);
    }

    public UserInfo getUserInfo(String login) {
        String sql = "select user_id, login, password_hash, registration_date, last_login_date, role from user where login = ?";

        return jdbcTemplate.queryForObject(sql, (user, rowNum) ->
                new UserInfo(
                        user.getString("login"),
                        user.getString("password_hash"),
                        user.getString("role")
                ), login);
    }


    public void updatePersonInfo(String login, PersonInfo personInfo) {
        Integer personID = getPersonID(login);

        String sql = "update person p set " +
                "p.name = coalesce(nullif(:name,''), p.name), " +
                "p.surname = coalesce(nullif(:surname,''), p.surname), " +
                "p.age = coalesce(nullif(:age,''), p.age), " +
                "p.sex = coalesce(nullif(:sex,''), p.sex), " +
                "p.interests = coalesce(nullif(:interests,''), p.interests), " +
                "p.city = coalesce(nullif(:city,''), p.city) " +
                "where p.person_id = :person_id;";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", personInfo.getName());
        parameters.put("surname", personInfo.getSurname());
        parameters.put("age", personInfo.getAge());
        parameters.put("sex", personInfo.getSex());
        parameters.put("interests", personInfo.getInterests());
        parameters.put("city", personInfo.getCity());
        parameters.put("person_id", personID);

        namedParameterJdbcTemplate.update(sql, parameters);
    }

//    public void updatePersonInfo(String login, PersonInfo personInfo) {
//        Integer personID = getPersonID(login);
//        String sql = "update person p set p.name = :name where p.person_id = :person_id";
//        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
//                .addValue("person_id", personID)
//                .addValue("name", personInfo.getName());
//        jdbcTemplate.update(sql, mapSqlParameterSource);
//
//    }
}

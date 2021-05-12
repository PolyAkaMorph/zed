package com.zed.repository;

import com.zed.dto.PersonInfo;
import com.zed.dto.RegistrationInfo;
import com.zed.dto.UserInfo;
import com.zed.model.Person;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class PreparedStatement {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Integer createNewUser(RegistrationInfo registrationInfo) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("user").usingGeneratedKeyColumns("user_id")
                .usingColumns("login", "password_hash","name", "surname", "age", "sex", "interests", "city");
        Map<String, Object> parameters = registrationInfo.getPersonInfoAsHashMap();
        return jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)).intValue();
    }

    public Integer getUserID(String login) {
        String sql = "select u.user_id from user u where u.login = :login";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("login", login);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, mapSqlParameterSource, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
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
        String sql = "update user u set " +
                "u.name =       coalesce(nullif(:name,''), u.name), " +
                "u.surname =    coalesce(nullif(:surname,''), u.surname), " +
                "u.age =        coalesce(nullif(:age,''), u.age), " +
                "u.sex =        coalesce(nullif(:sex,''), u.sex), " +
                "u.interests =  coalesce(nullif(:interests,''), u.interests), " +
                "u.city =       coalesce(nullif(:city,''), u.city) " +
                "where u.person_id = :person_id;";
        Map<String, Object> parameters = personInfo.getPersonInfoAsHashMap();
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    public PersonInfo getPersonInfo(String currentLogin) {
        String sql = "select u.login, u.name, u.surname, u.age, u.sex, u.interests, u.city " +
                "from user u " +
                "where u.login = ?;";

        return jdbcTemplate.queryForObject(sql, (personInfo, rowNum) -> new PersonInfo(
                personInfo.getString("login"),
                personInfo.getString("name"),
                personInfo.getString("surname"),
                personInfo.getString("age"),
                personInfo.getString("sex"),
                personInfo.getString("interests"),
                personInfo.getString("city"),
                false
        ), currentLogin);
    }

    public PersonInfo getPersonInfo(String currentLogin, String personLogin) {
        String sql = "select u2.login, u2.name, u2.surname, u2.age, u2.sex, u2.interests, u2.city, f.friendship_id " +
                "from user u " +
                "join user u2 " +
                "left join friendship f on f.user_id = u.user_id and f.friend_user_id = u2.user_id " +
                "where u.login = ? and u2.login = ?;";

        return jdbcTemplate.queryForObject(sql, (personInfo, rowNum) -> new PersonInfo(
                        personInfo.getString("login"),
                        personInfo.getString("name"),
                        personInfo.getString("surname"),
                        personInfo.getString("age"),
                        personInfo.getString("sex"),
                        personInfo.getString("interests"),
                        personInfo.getString("city"),
                        Strings.isEmpty(personInfo.getString("friendship_id"))
                ), currentLogin, personLogin);
    }

    public List<PersonInfo> getAllSearchedPersons(String currentLogin, String name, String surname) {
        name = name + "%";
        surname = surname + "%";
        String sql = "select u.login, u.name, u.surname, u.age, u.sex, u.interests, u.city " +
                "from user u " +
                "where u.login <> ?" +
                "and u.name like ? " +
                "and u.surname like ?" +
                "order by u.user_id ";

        return jdbcTemplate.query(sql,
                (personInfo, rownum) -> new PersonInfo(
                        personInfo.getString("login"),
                        personInfo.getString("name"),
                        personInfo.getString("surname"),
                        personInfo.getString("age"),
                        personInfo.getString("sex"),
                        personInfo.getString("interests"),
                        personInfo.getString("city"),
                        false)
                ,currentLogin, name, surname);
    }

    public List<PersonInfo> getAllPersons(String currentLogin) {
        String sql = "select u.login, u.name, u.surname, u.age, u.sex, u.interests, u.city " +
                "from user u " +
                "where u.login <> ? LIMIT 100;"; //todo remove limit

        return jdbcTemplate.query(sql,
                (personInfo, rownum) -> new PersonInfo(
                        personInfo.getString("login"),
                        personInfo.getString("name"),
                        personInfo.getString("surname"),
                        personInfo.getString("age"),
                        personInfo.getString("sex"),
                        personInfo.getString("interests"),
                        personInfo.getString("city"),
                        false)
                ,currentLogin);

    }

    public List<PersonInfo> getAllFriends(String currentLogin) {
        String sql = "select u2.login, u2.name, u2.surname, u2.age, u2.sex, u2.interests, u2.city " +
                    "from user u " +
                    "join friendship f on f.user_id = u.user_id " +
                    "join user u2 on u2.user_id = f.friend_user_id " +
                    "where u.login = ?;";
        return jdbcTemplate.query(sql,
                (personInfo, rownum) -> new PersonInfo(
                        personInfo.getString("login"),
                        personInfo.getString("name"),
                        personInfo.getString("surname"),
                        personInfo.getString("age"),
                        personInfo.getString("sex"),
                        personInfo.getString("interests"),
                        personInfo.getString("city"),
                        true)
                ,currentLogin);
    }

    public void setNewFriend(String currentLogin, String login) {
        String sql = "insert into friendship (user_id, friend_user_id) " +
                "select u.user_id, u2.user_id from user u, user u2 where u.login = :currentLogin and u2.login = :login;";
        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource()
                .addValue("currentLogin", currentLogin)
                .addValue("login", login));
    }

    public void removeFriend(String currentLogin, String friendToRemoveLogin) {
        String sql = "delete from friendship where user_id = " +
                "(select user_id from user where login = ?) and friend_user_id = (select user_id from user where login = ?);";
        jdbcTemplate.update(sql, currentLogin, friendToRemoveLogin);

    }
}

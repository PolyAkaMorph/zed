package com.zed.repository;

import com.zed.dto.PersonInfo;
import com.zed.dto.RegistrationInfo;
import com.zed.dto.UserInfo;
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
import java.util.Map;

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

    public Integer createNewPersonal(RegistrationInfo registrationInfo, Integer newUserId) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("person").usingGeneratedKeyColumns("person_id")
                .usingColumns("user_id", "name", "surname", "age", "sex", "interests", "city");
        Map<String, Object> parameters = registrationInfo.getPersonInfoAsHashMap();
        parameters.put("user_id",newUserId);
        return jdbcInsert.executeAndReturnKey(parameters).intValue();
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

    public Integer getPersonID(String login) {
        String sql = "select p.person_id from user u join person p on p.user_id = u.user_id where u.login = :login";
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
        Integer personID = getPersonID(login);

        String sql = "update person p set " +
                "p.name =       coalesce(nullif(:name,''), p.name), " +
                "p.surname =    coalesce(nullif(:surname,''), p.surname), " +
                "p.age =        coalesce(nullif(:age,''), p.age), " +
                "p.sex =        coalesce(nullif(:sex,''), p.sex), " +
                "p.interests =  coalesce(nullif(:interests,''), p.interests), " +
                "p.city =       coalesce(nullif(:city,''), p.city) " +
                "where p.person_id = :person_id;";
        Map<String, Object> parameters = personInfo.getPersonInfoAsHashMap();
        parameters.put("person_id", personID);

        namedParameterJdbcTemplate.update(sql, parameters);
    }

    public PersonInfo getPersonInfo(String currentLogin) {
        String sql = "select u.login, p.name, p.surname, p.age, p.sex, p.interests, p.city " +
                "from user u " +
                "join person p on p.user_id = u.user_id " +
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
        String sql = "select u.login, p.name, p.surname, p.age, p.sex, p.interests, p.city, f.user_id " +
                "from user u " +
                "join person p on p.user_id = u.user_id " +
                "join user u2 " +
                "left join friendship f on f.user_id = u2.user_id and f.friend_user_id = u.user_id " +
                "where u.login = ? and u2.login = ?;";

        return jdbcTemplate.queryForObject(sql, (personInfo, rowNum) -> new PersonInfo(
                        personInfo.getString("login"),
                        personInfo.getString("name"),
                        personInfo.getString("surname"),
                        personInfo.getString("age"),
                        personInfo.getString("sex"),
                        personInfo.getString("interests"),
                        personInfo.getString("city"),
                        Strings.isEmpty(personInfo.getString("user_id"))
                ), personLogin, currentLogin);
    }

    public List<PersonInfo> getAllPersons(String currentLogin) {
        String sql = "select u.login, p.name, p.surname, p.age, p.sex, p.interests, p.city " +
                "from user u " +
                "join person p on p.user_id = u.user_id " +
                "where u.login <> ?;";
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
        String sql = "select u2.login, p.name, p.surname, p.age, p.sex, p.interests, p.city " +
                    "from user u " +
                    "join friendship f on f.user_id = u.user_id " +
                    "join person p on p.user_id = f.friend_user_id " +
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
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("friendship").usingGeneratedKeyColumns("friendship_id")
                .usingColumns("user_id", "friend_user_id");
        Map<String, Object> parameters = new HashMap<>();
        // three base requests instead of one, but code is simpler
        parameters.put("user_id",getUserID(currentLogin));
        parameters.put("friend_user_id",getUserID(login));
        Integer newFriendshipId = jdbcInsert.executeAndReturnKey(parameters).intValue();

    }

    public void removeFriend(String currentLogin, String friendToRemoveLogin) {
        String sql = "delete from friendship f where f.user_id = ? and f.friend_user_id = ?;";
        // three base requests instead of one, but code is simpler
        Integer currentUserId = getUserID(currentLogin);
        Integer friendToRemoveID = getUserID(friendToRemoveLogin);
        jdbcTemplate.update(sql, currentUserId, friendToRemoveID);

    }
}

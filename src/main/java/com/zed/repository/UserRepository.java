package com.zed.repository;

import com.zed.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);
    User getByKey(Integer userId);
    User getByLoginAndHash(String login, String passwordHash);
    List<User> getAllUsers();
    boolean delete(User user);

}

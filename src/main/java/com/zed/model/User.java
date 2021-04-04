package com.zed.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    protected Integer userId;
    protected String login;
    protected String passwordHash;
    protected Date registrationDate;
    protected Date lastLoginDate;
    protected Role role;
}

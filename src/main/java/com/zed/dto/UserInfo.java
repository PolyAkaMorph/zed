package com.zed.dto;

import lombok.Data;

@Data
public class UserInfo {
    protected String login;
    protected String passwordHash;
    protected String role;

    public UserInfo(String login, String passwordHash, String role) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.role = role;
    }
}

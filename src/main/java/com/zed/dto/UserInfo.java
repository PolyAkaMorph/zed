package com.zed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    protected String login;
    protected String passwordHash;
    protected String role;
}

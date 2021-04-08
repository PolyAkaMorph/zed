package com.zed.dto;

import lombok.Data;

@Data
public class RegistrationInfo {
    protected String login;
    protected String password;
    protected String name;
    protected String surname;
    protected String age;
    protected String sex;
    protected String interests;
    protected String city;
}

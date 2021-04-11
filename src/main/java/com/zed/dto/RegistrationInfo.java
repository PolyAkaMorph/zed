package com.zed.dto;

import lombok.Data;


@Data
public class RegistrationInfo {
    protected String login;
    protected String password;
    protected String passwordCheck;
    protected String name;
    protected String surname;
    protected String age;
    protected String sex;
    protected String interests;
    protected String city;
    protected String error;

    public RegistrationInfo(String login, String password, String passwordCheck, String name, String surname, String age, String sex, String interests, String city, String error) {
        this.login = login;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.sex = sex;
        this.interests = interests;
        this.city = city;
        this.error = error;
    }

    public RegistrationInfo() {
    }

    public PersonInfo getPersonInfo() {
        return new PersonInfo(name, surname, age, sex, interests, city);
    }
}

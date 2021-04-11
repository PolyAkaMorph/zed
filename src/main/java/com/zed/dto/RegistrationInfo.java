package com.zed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public RegistrationInfo(PersonInfo personInfo) {
        this.name = personInfo.getName();
        this.surname = personInfo.getSurname();
        this.age = personInfo.getAge();
        this.sex = personInfo.getSex();
        this.interests = personInfo.getInterests();
        this.city = personInfo.getCity();
    }

    public PersonInfo getPersonInfo() {
        return new PersonInfo(name, surname, age, sex, interests, city, login);
    }
}

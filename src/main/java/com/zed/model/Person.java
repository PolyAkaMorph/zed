package com.zed.model;

import lombok.Data;

@Data
public class Person {
    protected Integer userId;
    protected Integer personalId;
    protected String name;
    protected String surname;
    protected Integer age;
    protected Sex sex;
    protected String interests;
    protected String city;
}

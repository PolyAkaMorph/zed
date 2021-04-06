package com.zed.dto;

import com.zed.model.Sex;
import lombok.Data;

@Data
public class PersonInfo {
    protected String name;
    protected String surname;
    protected String age;
    protected String sex;
    protected String interests;
    protected String city;
}

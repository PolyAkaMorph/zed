package com.zed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfo {
    protected String name;
    protected String surname;
    protected String age;
    protected String sex;
    protected String interests;
    protected String city;
}

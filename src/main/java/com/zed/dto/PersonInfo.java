package com.zed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

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
    protected String login;

    public Map<String, Object> getPersonInfoAsHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", this.getName());
        map.put("surname", this.getSurname());
        map.put("age", this.getAge());
        map.put("sex", this.getSex());
        map.put("interests", this.getInterests());
        map.put("city", this.getCity());
        map.put("login", this.getLogin());
        return map;
    }
}

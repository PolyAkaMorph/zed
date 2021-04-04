package com.zed.model;

import lombok.Data;

import java.util.List;

@Data
public class Friendship {
    protected Integer userID;
    protected List<Integer> friends;
}

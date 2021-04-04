package com.zed.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiV1 {
    @RequestMapping("/api/v1/")
    public String entry() {
        log.debug("entry");
        log.error("entry");
        log.info("entry");
        return "entry";
    }

    @RequestMapping("/api/v1/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/api/v1/me")
    public String me() {
        return "me";
    }

    @RequestMapping("/api/v1/all")
    public String all() {
        return "all";
    }

    @RequestMapping("/api/v1/friends")
    public String friends() {
        return "friends";
    }

    @RequestMapping("/api/v1/edit")
    public String edit() {
        return "edit";
    }

}


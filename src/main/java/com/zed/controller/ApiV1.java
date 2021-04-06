package com.zed.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ApiV1 {
    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("test", "test");
        return "all";
    }

    @GetMapping("/friends")
    public String getFriends(Model model) {
        model.addAttribute("test", "test");
        return "friends";
    }

    @GetMapping("/friend")
    public String getFriend(Model model) {
        model.addAttribute("test", "test");
        return "friend";
    }

    @GetMapping("/person")
    public String getPerson(Model model) {
        model.addAttribute("test", "test");
        return "person";
    }

    @GetMapping("/edit")
    public String getEdit(Model model) {
        model.addAttribute("test", "test");
        return "edit";
    }

}


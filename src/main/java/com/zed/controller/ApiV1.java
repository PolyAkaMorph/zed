package com.zed.controller;

import com.zed.dto.PersonInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ApiV1 {
    @GetMapping("/person")
    public String getPerson(Model model) {
        model.addAttribute("name", "Nikola");
        model.addAttribute("surname", "Tesla");
        model.addAttribute("age", "40");
        model.addAttribute("sex", "MALE");
        model.addAttribute("interests", "Electricity");
        model.addAttribute("city", "New York");
        return "person";
    }

    @GetMapping("/edit")
    public String getEdit(Model model) {
        model.addAttribute("personinfo", new PersonInfo());
        return "edit";
    }

    @PostMapping("/edit-submit")
    public String editSubmit(@ModelAttribute PersonInfo personInfo, Model model) {
        model.addAttribute(personInfo);
        log.debug(personInfo.getAge());
        return "redirect:/person";
    }

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



}


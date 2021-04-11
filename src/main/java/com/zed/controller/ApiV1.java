package com.zed.controller;

import com.zed.dto.PersonInfo;
import com.zed.dto.RegistrationInfo;
import com.zed.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class ApiV1 {
    @Autowired
    UserService userService;

    @GetMapping("/person")
    public String getPerson(Model model) {
        model.addAttribute("personinfo", userService.getCurrentPerson());
        return "person";
    }

    @GetMapping("/edit")
    public String getEdit(Model model) {
        model.addAttribute("personinfo", userService.getCurrentPerson());
        return "edit";
    }

    @PostMapping("/edit-submit")
    public String editSubmit(@ModelAttribute PersonInfo personInfo, Model model) {
        model.addAttribute(personInfo);
        userService.editPersonal(personInfo);
        return "redirect:/person";
    }

    @GetMapping("/registration")
    public String getRegister(Model model) {
        model.addAttribute("registrationinfo", new RegistrationInfo());
        return "registration";
    }

    @PostMapping("/registration-submit")
    public String registrationSubmit(@ModelAttribute RegistrationInfo registrationInfo, Model model) {
        if (userService.isUserReadyForRegister(registrationInfo)) {
            PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            registrationInfo.setPassword(delegatingPasswordEncoder.encode(registrationInfo.getPassword()));
            userService.register(registrationInfo);
            return "redirect:/login?register";
        }
        registrationInfo.setPassword(null);
        registrationInfo.setPasswordCheck(null);
        model.addAttribute("registrationinfo", registrationInfo);
        return "/registration";

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


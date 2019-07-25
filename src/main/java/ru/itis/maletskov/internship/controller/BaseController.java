package ru.itis.maletskov.internship.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/ybot")
    public String homePage() {
        return "ybot";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}

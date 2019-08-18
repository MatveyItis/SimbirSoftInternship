package ru.itis.maletskov.internship.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {
    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal UserDetails user) {
        if (user != null) {
            return "redirect:/chats";
        }
        return "login";
    }

    @GetMapping(value = {"/", "/home"})
    public String homePage() {
        return "home";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}

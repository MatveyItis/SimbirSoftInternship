package ru.itis.maletskov.internship.controller;

import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.maletskov.internship.form.UserForm;
import ru.itis.maletskov.internship.service.UserService;
import ru.itis.maletskov.internship.service.YouTubeService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final YouTubeService youTubeService;

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute UserForm userForm,
                               Model model) {
        userService.createUser(userForm);
        return "redirect:/login";
    }

    @PostMapping("/search_video")
    public String searchVideo(@RequestParam("videoName") String name,
                              Model model) throws IOException, JSONException {
        model.addAttribute("url", youTubeService.searchVideo(name.split(" ")[0], name.split(" ")[1]));
        return "home";
    }
}

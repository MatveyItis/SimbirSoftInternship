package ru.itis.maletskov.internship.controller;

import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.maletskov.internship.service.YouTubeService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class YBotController {
    private final YouTubeService youTubeService;

    @PostMapping("/ybot_command")
    public String searchVideo(@RequestParam("search_request") String name,
                              Model model) throws IOException, JSONException {
        model.addAttribute("url", youTubeService.searchVideo(name.split(" ")[0], name.split(" ")[1], false, false));
        return "ybot";
    }
}

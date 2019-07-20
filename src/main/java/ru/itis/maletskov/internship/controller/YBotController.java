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

    @PostMapping("/search_video")
    public String searchVideo(@RequestParam("videoName") String name,
                              Model model) throws IOException, JSONException {
        //NOSONAR todo remake that
        model.addAttribute("url", youTubeService.searchVideo(name.split(" ")[0], name.split(" ")[1]));
        return "home";
    }
}
